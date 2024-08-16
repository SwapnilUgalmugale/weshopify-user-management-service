package com.weshopify.platform.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.BrandsBean;
import com.weshopify.platform.service.BrandsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BrandsResource {

	private BrandsService brandsService;
	
	public BrandsResource(BrandsService brandsService) {
		this.brandsService = brandsService;
		}
	
	
	@Operation(summary = "findAllBrands", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/brands")
	public ResponseEntity<List<BrandsBean>> findAllBrands(){
		return ResponseEntity.ok(brandsService.findAllBrands());
	}
	
	@Operation(summary = "getBrandsById", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/brands/{brandId}")
	public ResponseEntity<BrandsBean> getBrandsById(@PathVariable("brandId") int brandId){
		return ResponseEntity.ok(brandsService.findBrandById(brandId));
	}
	
	@Operation(summary = "createBrand", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/brands")
	public ResponseEntity<BrandsBean> createBrand(@RequestBody BrandsBean brandsBean){
		log.info("brands bean data is:\t"+brandsBean.toString());
		return ResponseEntity.ok(brandsService.createBrand(brandsBean));
	}
	
	@Operation(summary = "updateBrands", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/brands")
	public ResponseEntity<BrandsBean> updateBrands(@RequestBody BrandsBean brandsBean){
		return ResponseEntity.ok(brandsService.updateBrand(brandsBean));
	}
	
	@Operation(summary = "deleteBrands", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/brands/{brandId}")
	public ResponseEntity<List<BrandsBean>> deleteBrands(@PathVariable("brandId") int brandId){
		return ResponseEntity.ok(brandsService.deleteBrand(brandId));
	}
	
	@Operation(summary = "cleandb", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/brands/cleandb")
	public ResponseEntity<Map<String,String>> cleandb(){
		brandsService.cleanDb();
		Map<String, String> messageMap = new HashMap<>();
		messageMap.put("message","All the records deleted from the database");
		return ResponseEntity.ok(messageMap);
	}
}
