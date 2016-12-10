package com.cloud.configure;

import com.cloud.ftp.FtpManager;
import lombok.extern.log4j.Log4j2;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;

/**
 * ftp server config
 * Created by micky on 11/29/16.
 */
@Configuration
@Log4j2
public class FtpConfiguration {

    @Autowired
    private FtpManager ftpServer;

    /**
     * run ftp server after tomcat execute
     * @throws FtpException
     */
    @EventListener({ApplicationReadyEvent.class})
    private void setting() throws FtpException {
        ftpServer.start();
    }



}
