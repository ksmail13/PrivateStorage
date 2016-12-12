package com.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.cloud.account", "com.cloud.configure", "com.cloud.file", "com.cloud.ftp"})
@EnableJpaRepositories(basePackages = {"com.cloud"})
@EnableTransactionManagement
@EnableJpaAuditing
public class StorageApplication  {

	public static void main(String[] args) {
		SpringApplication.run(StorageApplication.class, args);
	}


}
