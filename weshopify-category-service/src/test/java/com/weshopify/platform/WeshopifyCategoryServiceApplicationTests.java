package com.weshopify.platform;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import com.weshopify.platform.model.Category;
import com.weshopify.platform.repo.CategoriesRepository;

@SpringBootTest
class WeshopifyCategoryServiceApplicationTests {


	@Test
	void contextLoads() {
	}
}
