package com.weshopify.platform.validations;




import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private int statusCode;
	private Date timeStamp;

}
