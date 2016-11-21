package com.cloud.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by micky on 11/22/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String username;
    private List<? extends GrantedAuthority> authorities;
    private String token;

}
