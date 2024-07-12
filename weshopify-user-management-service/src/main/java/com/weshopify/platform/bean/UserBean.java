package com.weshopify.platform.bean;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
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
@Getter
@Setter
public class UserBean implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -7030543926525838489L;
	
	private String id;
	
	@NotEmpty(message = "{firstName.notEmpty.message}")
	private String firstName;
	@NotEmpty(message = "{lastName.notEmpty.message}")
    private String lastName;
	@NotEmpty(message = "{email.notEmpty.message}")
	private String[] emails;
	@NotEmpty(message = "{password.notEmpty.message}")
	private String password;
	@NotEmpty(message = "{userName.notEmpty.message}")
	private String userId;
	@NotEmpty(message = "{mobile.notEmpty.message}")
	private String mobile;
	
	private boolean status;
}
