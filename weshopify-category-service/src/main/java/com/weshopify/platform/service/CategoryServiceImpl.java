package com.weshopify.platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang.RandomStringUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.weshopify.platform.bean.CategoryBean;
import com.weshopify.platform.cqrs.commands.CategoryCommand;
import com.weshopify.platform.model.Category;
import com.weshopify.platform.repo.CategoriesRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	private CategoriesRepository catRepo;
	
	@Autowired
	private CommandGateway commandBus;

	public CategoryServiceImpl(CategoriesRepository catRepo) {
		this.catRepo = catRepo;
	}

	@Override
	public CategoryBean createCategory(CategoryBean catBean) {

		return convertEntityToBean(catRepo.save(convertBeanToEntity(catBean)));
	}

	@Override
	public CategoryBean updateCategory(CategoryBean catBean) {
		catBean = convertEntityToBean(catRepo.save(convertBeanToEntity(catBean)));
		CategoryCommand catCommand = createCommand(catBean);
		log.info("Step-1: Command Sending to the Command Handler");
		CompletableFuture<CategoryCommand> future = commandBus.send(catCommand);
		if(future.isDone()) {
			log.info("Category updates were delivered to consumer services");
		}else {
			log.error("Category updates were not delivered to consumers. They may retry the event store ");
		}
		return catBean;
	}

	@Override
	public List<CategoryBean> findAllCategories() {
		List<Category> catEntityList = catRepo.findAll();
		if (!CollectionUtils.isEmpty(catEntityList)) {
			List<CategoryBean> catBeanList = new ArrayList<>();
			catEntityList.stream().forEach(catEntity -> {
				catBeanList.add(convertEntityToBean(catEntity));
			});
			return catBeanList;
		} else {
			throw new RuntimeException("No Categories Found in Database");
		}

	}

	@Override
	public CategoryBean findCategoryById(int catId) {
		
		return convertEntityToBean(catRepo.findById(catId).get());
		
	}

	@Override
	public List<CategoryBean> findAllChilds(int parentId) {
		List<Category> catEntityList = catRepo.findChildsOfParent(parentId);
		if (!CollectionUtils.isEmpty(catEntityList)) {
			List<CategoryBean> catBeanList = new ArrayList<>();
			catEntityList.stream().forEach(catEntity -> {
				catBeanList.add(convertEntityToBean(catEntity));
			});
			return catBeanList;
		} else {
			throw new RuntimeException("No Categories Found For the Parent:\t "+parentId);
		}
	}

	@Override
	public List<CategoryBean> deleteCategory(int catId) {
		catRepo.deleteById(catId);
		return findAllCategories();
	}

	/**
	 * converting the bean to entity model
	 * 
	 * @param catBean
	 * @return
	 */

	private Category convertBeanToEntity(CategoryBean catBean) {

		Category catEntity = new Category();
		catEntity.setAlias(catBean.getAlias());
		catEntity.setEnabled(true);
		catEntity.setName(catBean.getName());
		if (catBean.getPcategory() > 0) {
			catEntity.setParent(catRepo.findById(catBean.getPcategory()).get());
		}

		// for update operation
		if (catBean.getId() > 0) {
			catEntity.setId(catBean.getId());
		}
		return catEntity;

	}

	/**
	 * converting the entity model to bean
	 * 
	 * @param catBean
	 * @return
	 */

	private CategoryBean convertEntityToBean(Category catEntity) {
		CategoryBean catBean = new CategoryBean();
				catBean.setAlias(catEntity.getAlias());
				catBean.setName(catEntity.getName());
				catBean.setId(catEntity.getId());
				catBean.setEnabled(catEntity.isEnabled());
				 if (catEntity.getParent() != null) {
				        catBean.setPcategory(catEntity.getParent().getId());
				    } else {
				        catBean.setPcategory(null); // Explicitly set as null if there's no parent
				    }

				    return catBean;
	}
	/**
	 * Converting the updated bean to command
	 * command user for creating events
	 * @param catBean
	 * @return
	 */
	private CategoryCommand createCommand(CategoryBean catBean) {
		CategoryCommand catCommand = new CategoryCommand ();
		catCommand.setAlias(catBean.getAlias());
		catCommand.setName(catBean.getName());
		String randomId = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
		catCommand.setEventId(randomId);
		catCommand.setId(catBean.getId());
		catCommand.setEnabled(catBean.isEnabled());
		catCommand.setPcategory(catBean.getPcategory());

		    return catCommand;
		 
	}

}
