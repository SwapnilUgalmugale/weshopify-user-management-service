package com.weshopify.platform.Dto;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

//@Data
public class Auditor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3327915416684660574L;

	@Temporal(value = TemporalType.TIMESTAMP )
	private Date createdDate;

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	private String createdBy;
	private String modifiedBy;
	 
}
