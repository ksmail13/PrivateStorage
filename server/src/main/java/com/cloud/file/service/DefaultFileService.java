package com.cloud.file.service;

import com.cloud.configure.IgnoreManager;
import com.cloud.configure.ServerConfig;
import com.cloud.file.FileType;
import com.cloud.file.model.*;
import com.cloud.file.repository.FileLogRepository;
import com.cloud.util.FileUtils;
import com.cloud.util.ImageUtil;
import com.cloud.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by micky on 2016. 12. 7..
 */
@Service
@Log4j2
public class DefaultFileService implements FileService {

    @Autowired
    private ServerConfig config;

    @Autowired
    private FileLogRepository logRepository;

    @Autowired
    private IgnoreManager manager;

    private ConcurrentHashMap<String, FileInfo> fileLock = new ConcurrentHashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        File file = new File(config.getSyncDirectory());
        FileInfoBuilder builder = new FileInfoBuilder();
        builder.setInnerFullPath(FileUtils.getCanonicalPath(file)).setType(FileType.DIRECTORY).setName("/");
        builder.setFullPath("/");
        builder.build();
    }

    @Override
    @Transactional
    public List<PublicFileInfo> getFileList(String subPath) throws FileNotFoundException {
        if(subPath == null) subPath = "";
        log.debug("request path : {}", config.getSyncDirectory()+"/"+subPath);
        File file = new File(config.getSyncDirectory()+"/"+subPath);

        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isDirectory()) throw new AccessViolationException(subPath);

        List<PublicFileInfo> fileList = new CopyOnWriteArrayList<>();
        Arrays.asList(file.listFiles()).parallelStream().forEach(f->{
            if(manager.isIgnoreFile(f.getName())) return;
            FileInfoBuilder builder = new FileInfoBuilder();
            String systemPath = FileUtils.getCanonicalPath(f);
            builder.setName(f.getName())
                    .setInnerFullPath(systemPath)
                    .setFullPath(systemPath.substring(config.getSyncDirectory().length()))
                    .setType(FileType.fileType(f));
            PublicFileInfo info = mapper.convertValue(builder.build(), PublicFileInfo.class);
            if(ImageUtil.isImageFile(f)) {
                try {
                    info.setThumbnail(ImageUtil.createThumbnail(config.getTempDirectory(), f, 300));
                } catch (IOException e) {
                    log.error("error while create image ({}) thumbnail", f.getName(), e);
                }
            }
            fileList.add(info);
        });

        return fileList;
    }

    @Override
    @Transactional
    public FileResponseInfo uploadFile(FileRequestInfo file) {
        String filePath = String.format("/%s/%s",file.getDirectory(), file.getFilename()).replace("//", "/");

        if(fileExistCheck(filePath))
            throw new FileExistException(filePath);

        FileInfoBuilder builder = new FileInfoBuilder();
        builder.setInnerFullPath(config.getSyncDirectory()+filePath)
                .setFullPath(filePath)
                .setType(FileType.FILE)
                .setName(file.getFilename());
        FileInfo info = builder.build();

        SuccessFileResponseInfo response = mapper.convertValue(file, SuccessFileResponseInfo.class);
        String workingId = UUID.randomUUID().toString();

        response.setWorkId(workingId);
        fileLock.put(workingId, info);

        saveLog(response, info);

        return response;
    }

    @Override
    @Transactional
    public FileResponseInfo updateFile(FileRequestInfo file) {
        String filePath = String.format("/%s/%s",file.getDirectory(), file.getFilename()).replace("//", "/");

        if(!fileExistCheck(filePath, true)) {
            throw new FileNotExistException(filePath);
        }
        if(fileUseCheck(filePath)) {
            throw new FileAlreadyUseException(filePath);
        }

        FileInfo info = FileInfoBuilder.find(filePath);
        String workingId = UUID.randomUUID().toString();
        fileLock.put(workingId, info);

        SuccessFileResponseInfo response = mapper.convertValue(file, SuccessFileResponseInfo.class);
        response.setWorkId(workingId);
        saveLog(response, info);
        return response;
    }


    @Override
    public void complete(String id) {
        if(!fileLock.containsKey(id)) {
            throw new LockException(id);
        }

        FileInfo info = fileLock.remove(id);
        saveLog(id, FileRequestType.COMPLETE, info.getInnerFullPath());
    }

    @Override
    public FileResponseInfo downloadFile(String path) {
        if(!fileExistCheck(path, true)) {
            throw new FileNotExistException(path);
        }
        if(fileUseCheck(path)) {
            throw new FileAlreadyUseException(path);
        }

        FileInfo info = FileInfoBuilder.find(path);
        String workingId = UUID.randomUUID().toString();
        fileLock.put(workingId, info);

        SuccessFileResponseInfo response = new SuccessFileResponseInfo();
        response.setWorkId(workingId);
        response.setFilename(FilenameUtils.getName(path));
        response.setDirectory(FilenameUtils.getFullPath(path));
        response.setType(FileRequestType.READ);
        saveLog(response, info);

        return response;
    }

    @Override
    public List<FileResponseInfo> multiUpdateFile(List<FileRequestInfo> request) {
        List<FileResponseInfo> responseInfoList = new ArrayList<>();
        for(FileRequestInfo req : request) {
            FileResponseInfo res;
            switch (req.getType()) {
                case CREATE:
                    try {
                        responseInfoList.add(uploadFile(req));
                    } catch (FileExistException e) {
                        responseInfoList.add(createErrorResponse(e, req.getDirectory()+"/"+req.getFilename()));
                    }
                    break;
                case UPDATE:
                case DELETE:
                    try {
                        responseInfoList.add(updateFile(req));
                    } catch(FileNotExistException | FileAlreadyUseException e) {
                        responseInfoList.add(createErrorResponse(e, req.getDirectory()+"/"+req.getFilename()));
                    }
                    break;
                case READ:
                    try {
                        responseInfoList.add(downloadFile(req.getDirectory()+"/"+req.getFilename()));
                    } catch(FileNotExistException | FileAlreadyUseException e) {
                        responseInfoList.add(createErrorResponse(e, req.getDirectory()+"/"+req.getFilename()));
                    }
                    break;
            }
        }

        return responseInfoList;
    }

    private ErrorFileResponseInfo createErrorResponse(Throwable e, String filename) {
        ErrorFileResponseInfo err = new ErrorFileResponseInfo();
        err.setError(e.getClass().getSimpleName());
        err.setMessage(e.getMessage());
        err.setFilename(filename);
        return err;
    }

    private void saveLog(FileResponseInfo file, FileInfo info) {
        saveLog(((SuccessFileResponseInfo)file).getWorkId(), ((SuccessFileResponseInfo)file).getType(), info.getInnerFullPath());
    }

    private void saveLog(String id, FileRequestType type, String filepath) {
        FileLog log = new FileLog();
        log.setWorkingId(id);
        log.setFilePath(filepath);
        log.setType(type);
        logRepository.save(log);
    }

    /**
     * check file lock if file is locked file using other client
     * @param filePath
     * @return
     */
    private boolean fileUseCheck(String filePath) {
        FileInfo info = FileInfoBuilder.find(filePath);
        return fileLock.containsValue(info);
    }

    /**
     * check file exist
     * if exist create file information instance
     * @param filePath file path
     * @return is file exist
     */
    private boolean fileExistCheck(String filePath) {
        return fileExistCheck(filePath, false);
    }


    /**
     * check file exist if realcheck is false then check file info
     * @param filePath
     * @param realCheck
     * @return
     */
    private boolean fileExistCheck(String filePath, boolean realCheck) {
        FileInfoBuilder builder = new FileInfoBuilder();
        File file = new File(config.getSyncDirectory()+filePath);
        boolean exist = file.exists();
        FileInfo cached = FileInfoBuilder.find(filePath);
        if(exist && cached == null) {
            builder.setInnerFullPath(FileUtils.getCanonicalPath(file))
                    .setFullPath(filePath)
                    .setName(file.getName())
                    .setType(FileType.fileType(file)).build();
        }

        if(realCheck) {
            return file.exists();
        } else {
            return file.exists() || cached != null;
        }
    }

}
