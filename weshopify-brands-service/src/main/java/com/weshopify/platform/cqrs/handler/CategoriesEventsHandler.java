package com.weshopify.platform.cqrs.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.weshopify.platform.bean.CategoryBean;
import com.weshopify.platform.cqrs.domainevents.CategoryEvent;
import com.weshopify.platform.repository.BrandsRepo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoriesEventsHandler {
	
	@Autowired
	private BrandsRepo brandsRepo;

	@EventHandler
	public CategoryEvent categoryEventHandler(CategoryEvent event) {
		log.info("Event recieved in Category Event Handler is "+event.toString());
		
		Set<CategoryBean> updatedCatList = new HashSet<CategoryBean>();
		
		brandsRepo.findAll().stream().forEach(brand->{
			if(!CollectionUtils.isEmpty(brand.getCategories())) {
				brand.getCategories().forEach(cat->{
					if(event.getId() == cat.getId()) {
					
						CategoryBean updatedCatBean = new CategoryBean();
						updatedCatBean.setId(event.getId());
						updatedCatBean.setAlias(event.getAlias());
						updatedCatBean.setName(event.getName());
						updatedCatBean.setPcategory(event.getPcategory());
						
						updatedCatList.add(updatedCatBean);
				  }else {
					  updatedCatList.add(cat);
				  }
				});
				brand.setCategories(updatedCatList);
				brandsRepo.save(brand);
			}
		});
		return null;
	}
	
	@QueryHandler
	public CategoryEvent queryCategoryEvents(CategoryEvent event) {
		log.info("Event recieved in Query category Event is "+event.toString());
		return null;
	}
}
