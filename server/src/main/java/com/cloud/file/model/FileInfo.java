package com.cloud.file.model;

import com.cloud.file.FileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Created by micky on 2016. 12. 11..
 */
@Data
@JsonIgnoreProperties({"fullPath"})
public final class FileInfo {

    private transient final String fullPath;

    @NonNull
    private final String name;

    private final FileType type;

    private final String thumbnail;

}
