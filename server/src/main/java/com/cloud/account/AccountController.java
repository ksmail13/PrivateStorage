package com.cloud.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by micky on 11/22/16.
 */
@Controller
public class AccountController {

    @RequestMapping("/access_token")
    @ResponseBody
    public AccessToken getAccessToken(@RequestBody LoginInfo info) {

        return new AccessToken();
    }
}
