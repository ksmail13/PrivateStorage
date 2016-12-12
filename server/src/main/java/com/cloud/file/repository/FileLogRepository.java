package com.cloud.file.repository;

import com.cloud.file.model.FileLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by micky on 2016. 12. 8..
 */
@Repository
public interface FileLogRepository extends JpaRepository<FileLog, Integer> {

}
