package com.weshopify.platform.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.weshopify.platform.model.Brands;

public interface BrandsRepo extends MongoRepository<Brands, Integer> {

}
