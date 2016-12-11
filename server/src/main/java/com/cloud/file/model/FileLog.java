package com.cloud.file.model;

import com.cloud.file.SyncType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by micky on 2016. 12. 8..
 */
@Entity(name = "filelog")
@Data
public class FileLog {

    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer id;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private SyncType type;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;

}
