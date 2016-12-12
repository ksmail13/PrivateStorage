package com.cloud.account;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by micky on 11/22/16.
 */
@Slf4j
@Service
public class AccessService implements UserDetailsService {
    @Value("${user.id}")
    private String username;
    @Value("${user.password}")
    private String password;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals(this.username))
            return new UserInformation(this.username, this.password);

        throw new UsernameNotFoundException(String.format("%s is not user", username));
    }



}

