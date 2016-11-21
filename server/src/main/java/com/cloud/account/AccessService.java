package com.cloud.account;

import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Service
public class AccessService implements UserDetailsService {
    @Value("${user.id}")
    private String username;
    @Value("${user.password}")
    private String password;

    public boolean checkUser(String username, String password) {
        log.debug("username {} password {}", this.username, this.password);
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals(this.username))
            return new UserInformation();

        throw new UsernameNotFoundException(String.format("%s is not user", username));
    }


    class UserInformation implements UserDetails {
        class UserGrantedAuthority implements GrantedAuthority {
            @Override
            public String getAuthority() {
                return "USER";
            }
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            HashSet<UserGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new UserGrantedAuthority());
            return Collections.unmodifiableSet(authorities);
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
