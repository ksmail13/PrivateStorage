package com.cloud.configure;

import com.cloud.util.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

/**
 * Created by micky on 2016. 12. 12..
 */

@Configuration
@EnableWebMvc
@Log4j2
public class ResourceConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ServerConfig config;

    @Autowired
    private ApplicationContext context;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String thumbnailPath = "file:///"+config.getTempDirectory()+"/";
        try {
            Resource r  = context.getResource(thumbnailPath);

            log.debug("add thumbnail path({}) to resource handler resource path (exist : {}){}",thumbnailPath, r.exists()
                    , FileUtils.getCanonicalPath(r.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        registry.addResourceHandler("/thumbnail/**").addResourceLocations(thumbnailPath);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        super.configurePathMatch(configurer);

        //configurer.setUseSuffixPatternMatch();
    }
}
