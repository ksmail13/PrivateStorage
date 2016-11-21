package com.cloud.account;

import com.cloud.util.UserNotFoundException;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by micky on 11/22/16.
 */
@Log4j2
@RestController
public class AccessController {

    @Autowired
    AccessService service;

    @RequestMapping(value = "/access_token", method = RequestMethod.POST)
    @ResponseBody
    public AccessToken getAccessToken(@RequestBody LoginInfo info, HttpSession session) {
        log.debug("access_token"+info);
        if(service.checkUser(info.getUserId(), info.getPassword())) {
            AccessService.UserInformation information =
                    (AccessService.UserInformation) service.loadUserByUsername(info.getUserId());

            return new AccessToken(information.getUsername(), information.getAuthorities(), session.getId());
        }

        throw new UserNotFoundException(info.getUserId());
    }
}
