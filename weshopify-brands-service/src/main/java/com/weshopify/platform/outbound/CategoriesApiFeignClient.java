package com.weshopify.platform.outbound;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

//@FeignClient(url = "${services.categories.base-uri}", value = "CategoriesApiFeignClient")
@FeignClient(name = "WESHOPIFY-CATEGORY-SERVICE" , value = "WESHOPIFY-CATEGORY-SERVICE")
public interface CategoriesApiFeignClient {

	@GetMapping(value = "/categories/{catId}")
	public ResponseEntity<String> findCategoryById(@PathVariable("catId") int catId,
			@RequestHeader final Map<String, String> headerMap);

}
