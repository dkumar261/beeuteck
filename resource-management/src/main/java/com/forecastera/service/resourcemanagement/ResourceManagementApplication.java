package com.forecastera.service.resourcemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ResourceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceManagementApplication.class, args);
	}

}
