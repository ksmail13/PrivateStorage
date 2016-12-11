package com.cloud.file.control;

import com.cloud.file.model.FileInfo;
import com.cloud.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * file api controller
 * Created by micky on 11/27/16.
 */
@RestController
@RequestMapping(path="/file", consumes = {"application/json"}, produces = {"application/json"})
public class FileController {

    @Autowired
    private FileService fileService;



    /**
     * request new file list
     * @return update file list
     */
    @RequestMapping(path = {"", "/{subPath}"}, method=RequestMethod.GET)
    @ResponseBody
    public List<FileInfo> getFileList(@PathVariable(required = false) String subPath) throws FileNotFoundException {
        return fileService.getFileList(subPath);
    }

    /**
     * Add new file update
     * @param uploadFiles client upload file
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FileInfo> uploadSync(List<FileInfo> uploadFiles) {
        // TODO : return files can upload
        return null;
    }

    /**
     * update files(change or delete)
     * @param updateFiles client update files
     */
    @RequestMapping(method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public List<FileInfo> updateSync(List<FileInfo> updateFiles) {
        // TODO : return files can update
        return null;
    }

    /**
     * sync finish signal
     * @param completeFiles
     * @return
     */
    @RequestMapping(path="/complete", method=RequestMethod.PUT)
    @ResponseBody
    public List<FileInfo> completeSync(@RequestBody List<FileInfo> completeFiles) {
        return null;
    }
}
