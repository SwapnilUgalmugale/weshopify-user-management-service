package com.weshopify.platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weshopify.platform.bean.BrandsBean;
import com.weshopify.platform.model.Brands;
import com.weshopify.platform.repository.BrandsRepo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BrandsServiceImpl implements BrandsService {
	
	@Autowired
	private BrandsRepo brandsRepo;
	
	@Override
	public BrandsBean createBrand(BrandsBean brandsBean) {
		return convertEntityToBean(brandsRepo.save(convertBeanToEntity(brandsBean)));
	}

	@Override
	public BrandsBean updateBrand(BrandsBean brandsBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BrandsBean> findAllBrands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BrandsBean findBrandById(int catId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BrandsBean> deleteBrand(int catId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * converting the bean to entity model
	 * 
	 * @param catBean
	 * @return
	 */

	private Brands convertBeanToEntity(BrandsBean brandsBean) {

		Brands brandsEntity = new Brands();
		brandsEntity.setName(brandsBean.getName());
		brandsEntity.setLogoPath(brandsBean.getLogoPath()); 
		brandsEntity.setCategories(brandsBean.getCategories());
		
		return brandsEntity;

	}
	

	/**
	 * converting the entity model to bean
	 * 
	 * @param catBean
	 * @return
	 */

	private BrandsBean convertEntityToBean(Brands brandsEntity) {

		BrandsBean brandsBean = new BrandsBean();
		brandsBean.setName(brandsEntity.getName());
		brandsBean.setLogoPath(brandsEntity.getLogoPath());
		brandsBean.setCategories(brandsEntity.getCategories());
		brandsBean.setId(brandsEntity.getId());
		
		return brandsBean;
				
	}
	

	
}
