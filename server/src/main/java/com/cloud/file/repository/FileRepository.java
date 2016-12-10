package com.cloud.file.repository;

import com.cloud.file.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by micky on 2016. 12. 8..
 */
@Repository
public interface FileRepository extends JpaRepository<FileInfo, Integer>{

    List<FileInfo> findByName(String name);
}
