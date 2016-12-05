package com.cloud.configure;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

/**
 * Created by micky on 11/29/16.
 */
@Configuration
@Data
@Log4j2
public class ServerConfig {

    @Value("${user.id}")
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    @Value("${user.password}")
    @Setter(AccessLevel.PRIVATE)
    private String password;

    @Value("${server.port}")
    @Setter(AccessLevel.PRIVATE)
    private String port;

    public int getPort() {
        return Integer.valueOf(port);
    }


    private String syncDirectory;

    /**
     * parse sync directory
     * ~ to home directory
     */
    @Autowired
    private void setSyncDirectory(@Value("${ftp.syncDirectory}") String syncDirectory) {
        log.info("sync directory {} ", syncDirectory);
        if(syncDirectory.charAt(0) == '~') {
            syncDirectory = syncDirectory.replaceFirst("~", System.getProperty("user.home"));
        }

        File syncDir = new File(syncDirectory);
        String realPath = null;
        if(!syncDir.exists()) {
            if (!syncDir.mkdirs()) {
                throw new IllegalStateException("Couldn't create sync directory "+syncDirectory);
            }
        }
        realPath = syncDir.getAbsolutePath();
        this.syncDirectory = realPath;
        log.info("parsed directory {}", this.syncDirectory);
    }

    @Value("#{'${file.allowext}'.split(',')}")
    @Setter(AccessLevel.PRIVATE)
    private List<String> allowExtensions;

    /**
     * ftp user idle time
     */
    @Value("${ftp.idleTime}")
    @Setter(AccessLevel.PRIVATE)
    private String idleTime;

    /**
     * ftp user database
     */
    @Value("${ftp.userProperties}")
    @Setter(AccessLevel.PRIVATE)
    private String userProperties;

    public int getIdleTime() {
        return Integer.valueOf(idleTime);
    }
}
