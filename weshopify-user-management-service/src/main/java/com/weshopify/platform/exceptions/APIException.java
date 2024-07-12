package com.weshopify.platform.exceptions;

import lombok.AllArgsConstructor;

import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorMsg;
	private int errorCode;

}
