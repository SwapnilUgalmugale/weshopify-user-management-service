package com.weshopify.platform.Dto;

import java.io.Serializable;

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
public class WSO2UserPersonals implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5318721528573238822L;
	
	private String givenName;
	private String familyName;

}
