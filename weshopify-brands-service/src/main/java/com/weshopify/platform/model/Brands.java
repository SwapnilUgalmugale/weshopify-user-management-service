package com.weshopify.platform.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Brands implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -929521821994144083L;
	
	@Id
	private int id;
	
	private String name;
	
	private String logoPath;
	
	private List<String> categories;

}
