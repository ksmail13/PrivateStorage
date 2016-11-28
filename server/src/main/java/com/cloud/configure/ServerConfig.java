package com.cloud.configure;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by micky on 11/29/16.
 */
@Component
@Data
public class ServerConfig {

    @Value("${user.id}")
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    @Value("${user.password}")
    @Setter(AccessLevel.PRIVATE)
    private String password;

    @Value("${local.server.port}")
    @Setter(AccessLevel.PRIVATE)
    private Integer port;

    @Value("${}")
    @Setter(AccessLevel.PRIVATE)
    private String syncDirectory;

    @Value("#{'${file.allowext}'.split(',')}")
    @Setter(AccessLevel.PRIVATE)
    private List<String> allowExtensions;
}
