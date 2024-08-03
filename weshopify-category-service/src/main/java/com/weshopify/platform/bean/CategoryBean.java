package com.weshopify.platform.bean;

import java.io.Serializable;
import lombok.Data;


@Data
//@Builder
public class CategoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String alias;
	private Integer pcategory;
	private boolean enabled;

}
