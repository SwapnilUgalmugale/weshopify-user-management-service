package com.weshopify.platform.model;

import java.io.Serializable;




import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class WSO2UserAuthnBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String grant_type;
	private String username;
	private String password;
	
	private String scope;

}
