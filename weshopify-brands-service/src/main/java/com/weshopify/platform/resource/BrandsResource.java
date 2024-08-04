package com.weshopify.platform.resource;

import java.util.List;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BrandsResource {

	private BrandsService brandsService;
	
	public BrandsResource(BrandsService brandsService) {
		this.brandsService = brandsService;
		}
	
	@GetMapping("/brands")
	public ResponseEntity<List<BrandsBean>> findAllBrands(){
		return ResponseEntity.ok(brandsService.findAllBrands());
	}
	
	@GetMapping("/brands/{brandId}")
	public ResponseEntity<BrandsBean> getBrandsById(@PathVariable("brandId") int brandId){
		return ResponseEntity.ok(brandsService.findBrandById(brandId));
	}
	
	@PostMapping("/brands")
	public ResponseEntity<BrandsBean> createBrand(@RequestBody BrandsBean brandsBean){
		log.info("brands bean data is:\t"+brandsBean.toString());
		return ResponseEntity.ok(brandsService.createBrand(brandsBean));
	}
	
	@PutMapping("/brands")
	public ResponseEntity<BrandsBean> updateBrands(@RequestBody BrandsBean brandsBean){
		return ResponseEntity.ok(brandsService.updateBrand(brandsBean));
	}
	
	@DeleteMapping("/brands/{brandId}")
	public ResponseEntity<List<BrandsBean>> deleteBrands(@PathVariable("brandId") int brandId){
		return ResponseEntity.ok(brandsService.deleteBrand(brandId));
	}
}
