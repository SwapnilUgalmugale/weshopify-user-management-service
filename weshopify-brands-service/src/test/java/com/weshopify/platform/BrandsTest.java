package com.weshopify.platform;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.weshopify.platform.outbound.CategoriesApiClient;

public class BrandsTest extends WeshopifyBrandsServiceApplicationTests {
	
	@Value("${services.categories.access-token}")
	private String accessToken;
	
	@Autowired
	private CategoriesApiClient apiClient;
	
	@Test
	public void testCategoriesById() {
		
		String catResp = apiClient.findCategoryById(accessToken, 1);
		
		assertNotNull(catResp);
	}

}
