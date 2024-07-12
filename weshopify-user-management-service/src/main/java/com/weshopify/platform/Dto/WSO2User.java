package com.weshopify.platform.Dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weshopify.platform.bean.RoleBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WSO2User implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 575624655932290588L;
	
	
	
	@JsonIgnore
	private String id;
	
	private String[] schemas;
	private String userName;
	private String password;
	private String[] emails;

	private WSO2UserPersonals name; 
	@JsonIgnore
	private List<RoleBean> roles;
	private List<WSO2PhoneNumbers> phoneNumbers;
	
}
 