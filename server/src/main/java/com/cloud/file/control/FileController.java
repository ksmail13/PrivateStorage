package com.cloud.file.control;

import com.cloud.file.model.FileInfo;
import com.cloud.file.model.FileRequestInfo;
import com.cloud.file.model.FileResponseInfo;
import com.cloud.file.model.PublicFileInfo;
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
    @RequestMapping(path = {"/list", "/{subPath}/list"}, method=RequestMethod.GET)
    @ResponseBody
    public List<PublicFileInfo> getFileList(@PathVariable(required = false) String subPath) throws FileNotFoundException {
        return fileService.getFileList(subPath);
    }

    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public FileResponseInfo downloadSync(@RequestParam String path) {
        return fileService.downloadFile(path);
    }

    /**
     * Add new file update
     * @param request client upload file information
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FileResponseInfo uploadSync(@RequestBody FileRequestInfo request) {
        return fileService.uploadFile(request);
    }

    /**
     * update files(change or delete)
     * @param request client update file information
     */
    @RequestMapping(method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FileResponseInfo updateSync(@RequestBody FileRequestInfo request) {
        return fileService.updateFile(request);
    }

    @RequestMapping(path="/multi", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<FileResponseInfo> multiUpdateSync(@RequestBody List<FileRequestInfo> request) {
        return fileService.multiUpdateFile(request);
    }

    /**
     * sync finish signal
     * @param id work Id
     */
    @RequestMapping(path="/complete", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void completeSync(@RequestParam String id) {
        fileService.complete(id);
    }
}
