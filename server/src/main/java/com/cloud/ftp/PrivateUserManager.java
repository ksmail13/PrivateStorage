package com.cloud.ftp;

import com.cloud.configure.ServerConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.AbstractUserManager;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Ftp server user manager
 * act minimum
 */
@Log4j2
@Component
public class PrivateUserManager implements UserManager {

    @Autowired
    private ServerConfig config;

    private User user;

    @PostConstruct
    private void setUser() {
        user = new BaseUser();
        BaseUser tmp = (BaseUser) user;
        tmp.setName(config.getUserId());
        tmp.setPassword(config.getPassword());
        tmp.setEnabled(true);
        tmp.setHomeDirectory(config.getSyncDirectory());
        tmp.setMaxIdleTime(config.getIdleTime());
    }

    @Override
    public User getUserByName(String username) throws FtpException {
        if(user.getName().equals(username))
            return user;
        return null;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return new String[]{user.getName()};
    }

    @Override
    public void delete(String username) throws FtpException {
        log.error("user delete not allow");
    }

    @Override
    public void save(User user) throws FtpException {
        log.error("user create not allow");
    }

    @Override
    public boolean doesExist(String username) throws FtpException {
        return user.getName().equals(username);
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        log.debug(authentication);
        return user;
    }

    @Override
    public String getAdminName() throws FtpException {
        return user.getName();
    }

    @Override
    public boolean isAdmin(String username) throws FtpException {
        return user.getName().equals(username);
    }
}
