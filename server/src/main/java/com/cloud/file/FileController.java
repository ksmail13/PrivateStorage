package com.cloud.file;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * file api controller
 * Created by micky on 11/27/16.
 */
@Controller
@RequestMapping(value = "/file", consumes = {"application/json"}, produces = {"application/json"})
public class FileController {

    /**
     * request new file list
     * @param baseTime client's last sync time
     * @return update file list
     */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public List<FileInfo> downloadSync(@RequestParam long baseTime) {

        // TODO : return update filelist
        return null;
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
    @RequestMapping(name="/complete", method=RequestMethod.PUT)
    public List<FileInfo> completeSync(List<FileInfo> completeFiles) {
        return null;
    }
}
