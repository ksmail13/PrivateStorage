package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.cloud.account", "com.cloud.configure", "com.cloud.file", "com.cloud.ftp"})
@EnableJpaRepositories(basePackages = {"com.cloud"})
@EnableTransactionManagement
@EnableWebMvc
@EnableJpaAuditing
public class StorageApplication  {

	public static void main(String[] args) {
		SpringApplication.run(StorageApplication.class, args);
	}


}
