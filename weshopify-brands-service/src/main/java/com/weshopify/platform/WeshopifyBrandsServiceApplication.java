package com.weshopify.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableMongoRepositories
@EnableFeignClients
public class WeshopifyBrandsServiceApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(WeshopifyBrandsServiceApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() { 
		return new RestTemplate();
	}
	
//	@Bean
//    ObjectMapper objectMapper() {
//			return new ObjectMapper();
//	}
}
