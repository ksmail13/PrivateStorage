package com.cloud.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by micky on 2016. 12. 13..
 */
class UserInformation implements UserDetails {
    private String username;
    private String password;

    UserInformation(String id, String password) {
        username = id;
        this.password = password;
    }
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
