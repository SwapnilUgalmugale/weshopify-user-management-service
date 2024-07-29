package com.weshopify.platform.Dto;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
@Builder 
public class Role implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 7245611904547709510L;
	
	private String display;
	
	private String value;
	
	@JsonProperty("$ref")
	private String ref;

}
