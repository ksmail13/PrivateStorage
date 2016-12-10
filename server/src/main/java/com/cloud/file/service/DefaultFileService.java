package com.cloud.file.service;

import com.cloud.configure.ServerConfig;
import com.cloud.file.model.FileInfo;
import com.cloud.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by micky on 2016. 12. 7..
 */
@Service
public class DefaultFileService implements FileService {

    @Autowired
    private ServerConfig config;

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional
    public List<FileInfo> getFileList() {
        File file = new File(config.getSyncDirectory());
        ArrayList<FileInfo> infoList = new ArrayList<>();
        for(File f : file.listFiles()) {
            List<FileInfo> info = fileRepository.findByName(f.getName());
            infoList.add(info.get(0));
        }

        return infoList;
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
