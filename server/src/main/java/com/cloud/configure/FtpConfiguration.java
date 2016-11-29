package com.cloud.configure;

import lombok.extern.log4j.Log4j2;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ftp server config
 * Created by micky on 11/29/16.
 */
@Configuration
@Log4j2
public class FtpConfiguration {

    @Autowired
    private ServerConfig config;

    @Autowired
    private UserManager userManager;

    private FtpServer ftpServer;

    @PostConstruct
    private void setting() throws FtpException {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory listenerFactory = new ListenerFactory();

        listenerFactory.setPort(config.getPort()+1);

        serverFactory.addListener("default", listenerFactory.createListener());
        serverFactory.setUserManager(userManager);

        ftpServer = serverFactory.createServer();
        ftpServer.start();
        log.debug("ftp running port = {}", config.getPort()+1);
    }

}
