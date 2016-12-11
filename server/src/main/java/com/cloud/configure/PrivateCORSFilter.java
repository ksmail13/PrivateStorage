package com.cloud.configure;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by micky on 11/22/16.
 */
@Component
public class PrivateCORSFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        if("OPTIONS".equals(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
        }

        chain.doFilter(request, response);
    }

}
