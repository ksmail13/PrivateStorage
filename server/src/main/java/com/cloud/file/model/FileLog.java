package com.cloud.file.model;

import com.cloud.file.SyncType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by micky on 2016. 12. 8..
 */
@Entity(name = "filelog")
@EntityListeners({AuditingEntityListener.class})
@Data
public class FileLog {

    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer id;

    @Column(updatable = false, nullable = false)
    private String workingId;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private FileRequestType type;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private long time;

}
