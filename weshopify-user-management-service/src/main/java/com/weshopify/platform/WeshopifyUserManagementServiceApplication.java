package com.weshopify.platform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class WeshopifyUserManagementServiceApplication {
	
	@Value("${iam-server.user-name}")
    private String username;

    @Value("${iam-server.password}")
    private String password;

	public static void main(String[] args) {
		SpringApplication.run(WeshopifyUserManagementServiceApplication.class, args);
	}
	@Bean
    public RestTemplate restTemplate() {
		 RestTemplate restTemplate = new RestTemplate();
	        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password));
	        return restTemplate;
    }
	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
