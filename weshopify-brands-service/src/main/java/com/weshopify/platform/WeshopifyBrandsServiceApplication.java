package com.weshopify.platform;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.weshopify.platform.model.Brands;
import com.weshopify.platform.repository.BrandsRepo;

@SpringBootApplication
@EnableMongoRepositories
public class WeshopifyBrandsServiceApplication implements CommandLineRunner {

	@Autowired
	private BrandsRepo repo;
	
	public static void main(String[] args) {
		SpringApplication.run(WeshopifyBrandsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Brands brand = new Brands();
		brand.setName("Amazon");
		brand.setLogoPath(null);
		brand.setCategories(Arrays.asList("Mobile, Computers","Electronics"));
		
		repo.save(brand);
		
	}

}
