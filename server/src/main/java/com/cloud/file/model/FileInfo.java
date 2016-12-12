package com.cloud.file.model;

import com.cloud.file.FileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * Desktop's file info
 * Created by micky on 2016. 12. 11..
 */
@Data(staticConstructor = "createInstance")
@JsonIgnoreProperties({"innerFullPath"})
public final class FileInfo {

    private transient final String innerFullPath;

    private final String fullPath;

    @NonNull
    private final String name;

    private final FileType type;

}
