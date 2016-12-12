package com.cloud.file.model;

import com.cloud.file.FileType;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by micky on 2016. 12. 13..
 */
@Data
public class PublicFileInfo {

    private String fullPath;

    @NonNull
    private String name;

    private FileType type;

    private String thumbnail;
}
