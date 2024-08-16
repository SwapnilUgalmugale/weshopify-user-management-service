package com.weshopify.platform.outbound;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoriesApiClient {

	@Autowired
	private RestTemplate rt;
	
	@Value("${services.categories.base-uri}")
	private String categoriesUri;
	
	@Autowired
	private HttpServletRequest request;
	
	public String findCategoryById(String access_token,int catId) {
		/**
		 * step-1: Prepare the Authorization Header
		 */
		String headerWithBearer = request.getHeader(HttpHeaders.AUTHORIZATION);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, headerWithBearer);
		
		/**
		 * step-2: Prepare the Request Body
		 */
		HttpEntity<String> requestBody = new HttpEntity<>(headers);
		
		
		/**
		 * step-3: Invoke the  categories api
		 */
		String findCatByIdUri = categoriesUri+"/"+catId;
		log.info("get categories uri is {}",findCatByIdUri);
		
		long startTime = System.currentTimeMillis();
		
		ResponseEntity<String> catResp = rt.exchange(findCatByIdUri, HttpMethod.GET, requestBody, String.class);
        long endTime = System.currentTimeMillis();
		log.info("Total time taken to execute the api call is {}",(endTime-startTime));
		log.info("response code is {}",catResp.getStatusCode().value());
		
		if(HttpStatus.OK.value() == catResp.getStatusCode().value()) {
			 String respBody = catResp.getBody();
			 log.info("Categories Service api response is {}",respBody);
			return respBody;
		}else {
			log.error("No Category Found with Category Id "+catId);
			throw new RuntimeException("No Category Found with Category Id "+catId);
		}
	}
}
