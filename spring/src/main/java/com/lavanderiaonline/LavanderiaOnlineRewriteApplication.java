package com.lavanderiaonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LavanderiaOnlineRewriteApplication {

	public static void main(String[] args) {
		SpringApplication.run(LavanderiaOnlineRewriteApplication.class, args);
	}

}
