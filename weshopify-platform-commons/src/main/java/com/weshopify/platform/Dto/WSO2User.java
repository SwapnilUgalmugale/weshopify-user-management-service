package com.weshopify.platform.Dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;



import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Data
@Builder
public class WSO2User implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 575624655932290588L;
	
	
	
	//@JsonIgnore
	private String id;
	
	private String[] schemas;
	private String userName;
	private String password;
	private String[] emails;

	private WSO2UserPersonals name; 
	
	@JsonIgnore
	private Map<String, String> meta;
	//@JsonIgnore
	private List<Role> roles;
	private List<WSO2PhoneNumbers> phoneNumbers;
	
}
 