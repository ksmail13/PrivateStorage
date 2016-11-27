package com.cloud.file;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
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
    @GeneratedValue
    @Column(name="id", unique = true, nullable = false)
    private int fileId;

    @Enumerated(EnumType.STRING)
    private FileType type;

    private final String name;


    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime createTime;

    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime modifiedTime;

}
