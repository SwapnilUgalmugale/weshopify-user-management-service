package com.weshopify.platform;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import com.weshopify.platform.model.Category;
import com.weshopify.platform.repo.CategoriesRepository;

public class TestCategories extends WeshopifyCategoryServiceApplicationTests {
	@Autowired
	private CategoriesRepository catRepo;

	private Category cat = null;

	@BeforeTestMethod(value = "testCreatedCategory")
	public void init() {
		Category parentCat = catRepo.findById(1).get();
		Category cat = new Category();
		cat.setName("test");
		cat.setAlias("test-alias");
		cat.setEnabled(true);
		cat.setParent(parentCat);
	
		catRepo.save(cat);
	}

	

	public void testCreatedCategory() {

		catRepo.save(cat);
		assertNotNull(cat);
		assertNotNull(cat.getId());
	}

}
