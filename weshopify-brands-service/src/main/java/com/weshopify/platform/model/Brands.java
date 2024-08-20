package com.weshopify.platform.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.weshopify.platform.bean.CategoryBean;

import lombok.Data;

@Document(collection = "brands")
@Data
public class Brands implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -929521821994144083L;
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private String name;
	
	private String logoPath;
	
	private Set<CategoryBean> categories;

}
