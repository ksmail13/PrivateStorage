package com.cloud.ftp;

import com.cloud.configure.ServerConfig;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by micky on 12/6/16.
 */
@Component
@Log4j2
public class FtpManager {


    @Autowired
    private ServerConfig config;

    @Autowired
    private ApplicationContext context;

    private ReentrantLock ftpServerLock = new ReentrantLock();

    @Getter
    private int port;

    private FtpServer ftpServer;

    @PostConstruct
    private void init() throws FtpException {

    }

    @PreDestroy
    private void destroy() {
        stop();
    }


    /**
     * start ftp server if it is not working
     * @throws FtpException
     */
    public void start() throws FtpException {
        try {
            ftpServerLock.lockInterruptibly();

            if (ftpServer == null) {
                FtpServerFactory serverFactory = new FtpServerFactory();
                serverFactory.addListener("default", getListener());
                serverFactory.setUserManager(getUserManager());

                ftpServer = serverFactory.createServer();
            }
            if (ftpServer.isStopped()) {
                ftpServer.start();
            }
        } catch (InterruptedException e) {
            log.error("start lock error {}", e);
        } finally {
            ftpServerLock.unlock();
        }

    }

    /**
     * stop ftp server
     */
    public void stop() {
        try {
            ftpServerLock.lockInterruptibly();

            if(!ftpServer.isStopped()) {
                ftpServer.stop();
            }

        } catch (InterruptedException e) {
            log.error("stop lock error {}", e);
        } finally {
            ftpServerLock.unlock();
        }
    }

    /**
     * create listener(@link{org.apache.ftpserver.listener.Listener})
     * port will set local server port +1
     * @see Listener
     * @return
     */
    private Listener getListener() {
        ListenerFactory listenerFactory = new ListenerFactory();
        port = ((AnnotationConfigEmbeddedWebApplicationContext)context).getEmbeddedServletContainer().getPort()+1;
        listenerFactory.setPort(port);
        log.info("ftp running on {}", port);
        return listenerFactory.createListener();
    }

    /**
     * create usermanager
     * @return
     * @throws FtpException
     */
    private UserManager getUserManager() throws FtpException {
        File property = new File(config.getUserProperties());
        if(!property.exists()) {
            try {
                property.createNewFile();
            } catch (IOException e) {
                log.error(e);
            }
        }
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        UserManager manager = null;
        userManagerFactory.setAdminName(config.getUserId());
        userManagerFactory.setFile(property);

        manager = userManagerFactory.createUserManager();

        manager.save(initUser());

        return manager;
    }

    /**
     * create user object
     * @return
     */
    private User initUser() {
        BaseUser user = new BaseUser();
        user.setName(config.getUserId());
        user.setPassword(config.getPassword());
        user.setMaxIdleTime(config.getIdleTime());
        user.setHomeDirectory(config.getSyncDirectory());
        user.setEnabled(true);
        return user;
    }
}
