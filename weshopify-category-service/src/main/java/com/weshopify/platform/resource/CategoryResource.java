package com.weshopify.platform.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.CategoryBean;
import com.weshopify.platform.service.CategoryService;

@RestController
public class CategoryResource {
	
	private CategoryService catService;
	
	public CategoryResource(CategoryService catService) {
		this.catService = catService;
	}
	
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryBean>> findAllCategories(){
		return ResponseEntity.ok(catService.findAllCategories());
	}
	
	@GetMapping("/categories/childs/{parentId}")
	public ResponseEntity<List<CategoryBean>> findChildCategories(@PathVariable("parentId") int parentId){
		return ResponseEntity.ok(catService.findAllChilds(parentId));
	}
	
	@PostMapping("/categories")
	public ResponseEntity<CategoryBean> createCategory(@RequestBody CategoryBean catBean){
		return ResponseEntity.ok(catService.createcategory(catBean));
	}
	
	@PutMapping("/categories")
	public ResponseEntity<CategoryBean> updateCategory(@RequestBody CategoryBean catBean){
		return ResponseEntity.ok(catService.updateCategory(catBean));
	}
	
	@DeleteMapping("/categories/{catId}")
	public ResponseEntity<List<CategoryBean>> deleteCategory(@PathVariable("catId") int catId){
		return ResponseEntity.ok(catService.deleteCategory(catId));
	}
	
	@GetMapping("/categories/{catId}")
	public ResponseEntity<CategoryBean> getById(@PathVariable("catId") int catId){
		return ResponseEntity.ok(catService.findCategoryById(catId));
	}

}
