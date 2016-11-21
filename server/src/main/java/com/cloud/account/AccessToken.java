package com.cloud.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by micky on 11/22/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private String token;

}
