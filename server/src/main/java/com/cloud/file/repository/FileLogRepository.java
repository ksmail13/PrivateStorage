package com.cloud.file.repository;

import com.cloud.file.model.FileLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by micky on 2016. 12. 8..
 */
public interface FileLogRepository extends JpaRepository<FileLog, Integer> {

}
