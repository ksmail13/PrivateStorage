package com.cloud.file.service;

import com.cloud.configure.IgnoreManager;
import com.cloud.configure.ServerConfig;
import com.cloud.file.FileType;
import com.cloud.file.model.FileInfo;
import com.cloud.file.model.FileInfoBuilder;
import com.cloud.util.FileUtils;
import com.cloud.util.ImageUtil;
import com.cloud.util.exception.AccessViolationException;
import lombok.extern.log4j.Log4j2;
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

/**
 * Created by micky on 2016. 12. 7..
 */
@Service
@Log4j2
public class DefaultFileService implements FileService {

    @Autowired
    private ServerConfig config;

    @Autowired
    private IgnoreManager manager;

    @PostConstruct
    private void init() {
        File file = new File(config.getSyncDirectory());
        FileInfoBuilder builder = new FileInfoBuilder();
        builder.setFullPath(FileUtils.getCanonicalPath(file)).setType(FileType.DIRECTORY).setName("/");
        builder.build();
    }

    @Override
    @Transactional
    public List<FileInfo> getFileList(String subPath) throws FileNotFoundException {
        if(subPath == null) subPath = "";
        log.debug("request path : {}", config.getSyncDirectory()+"/"+subPath);
        File file = new File(config.getSyncDirectory()+"/"+subPath);

        if(!file.exists()) throw new FileNotFoundException();
        if(!file.isDirectory()) throw new AccessViolationException(subPath);

        List<FileInfo> fileList = new ArrayList<>();
        Arrays.asList(file.listFiles()).parallelStream().forEach(f->{
            if(manager.isIgnoreFile(f.getName())) return;
            FileInfoBuilder builder = new FileInfoBuilder();
            builder.setName(f.getName())
                    .setFullPath(FileUtils.getCanonicalPath(f))
                    .setType(f.isDirectory()?FileType.DIRECTORY:FileType.FILE);

            if(ImageUtil.isImageFile(f)) {
                try {
                    builder.setThumbnail(ImageUtil.createThumbnail(f, 300));
                } catch (IOException e) {
                    log.error("error while create image ({}) thumbnail", f.getName(), e);
                }
            }
            fileList.add(builder.build());
        });

        return fileList;
    }

    @Override
    @Transactional
    public FileInfo uploadFile(FileInfo file) {

        return null;
    }

    @Override
    @Transactional
    public FileInfo updateFile(FileInfo file) {
        return null;
    }
}
