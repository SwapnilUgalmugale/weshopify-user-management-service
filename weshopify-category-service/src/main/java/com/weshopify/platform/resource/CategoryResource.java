package com.weshopify.platform.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.CategoryBean;

@RestController
public class CategoryResource {
	
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryBean>> findAllCategories(){
		
		CategoryBean catBean = CategoryBean.builder().name("test-cat").alias("test").enabled(true).build();
		List<CategoryBean> catList = new ArrayList<>();
		catList.add(catBean);
		return ResponseEntity.ok(catList);
	}

}
