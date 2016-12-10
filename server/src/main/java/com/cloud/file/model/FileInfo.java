package com.cloud.file.model;

import com.cloud.file.FileType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * Created by micky on 11/27/16.
 */
@Data
@Entity(name = "file")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="file_id", unique = true, nullable = false)
    private Integer fileId;

    @Enumerated(EnumType.STRING)
    private FileType type;

    @Column(unique = true, nullable = false)
    @NonNull
    private String name;

    private Boolean using;

    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime modifiedTime;

}

