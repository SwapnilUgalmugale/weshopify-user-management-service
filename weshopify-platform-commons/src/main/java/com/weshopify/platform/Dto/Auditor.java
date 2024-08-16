package com.weshopify.platform.Dto;

import java.io.Serializable;
import java.util.Date;

//@Data
public class Auditor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3327915416684660574L;

	
	private Date createdDate;

	
	private Date modifiedDate;
	
	private String createdBy;
	private String modifiedBy;
	 
}
