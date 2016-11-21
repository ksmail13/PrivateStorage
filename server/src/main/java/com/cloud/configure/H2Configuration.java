package com.cloud.configure;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by micky on 11/22/16.
 */
@Configuration
public class H2Configuration {
    public final static String H2_CONSOLE_URL = "/private/console/*";
    /**
     * add h2 database control console page on servlet page mapping
     * @return
     */
    @Bean
    ServletRegistrationBean h2ServletRegistration() {

        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings(H2_CONSOLE_URL);
        return registrationBean;
    }
}
