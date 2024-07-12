package com.weshopify.platform.bean;

import java.io.File;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;


// author SWAPNIL
// since: 20-5-2024
// apiNote: WeshopifyPlatformUser
// summary: PlatformUserBean is used to hold user's data
// and will be saved this user in wso2 IAm
//

@Data
@Builder
public class weshopifyPlatformUserBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 2083047031686267381L;

	private int userId;
	private String eamil;
	private String firstName;
	private String lastName;
	private String password;
	private String role;
	private boolean status;
	private File photos;
}

