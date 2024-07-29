package com.weshopify.platform.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weshopify.platform.Dto.Role;
import com.weshopify.platform.Dto.WSO2User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtAuthenticationService {

	private RedisTemplate<String, String> redisTemplate;
	HashOperations<String, String, String> hashOps = null;

	@Autowired
	private ObjectMapper mapper;

	private static final String JWT_TOKEN_HEADER_NAME = "Authorization";
	private static final String JWT_TOKEN_TYPE = "Bearer ";
	private static final String JWT_TOKEN_EXPIRY_KEY = "tokenExpiry";
	private static final String USER_ROLES_KEY = "USER_ROLES";
	private static final String USER_SUBJECT_NAME = "SUBJECT";

	JwtAuthenticationService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		hashOps = redisTemplate.opsForHash();
	}

	public Authentication authenticateUser(HttpServletRequest request) {
		Authentication authn = null;
		String token = resolveToken(request);

		boolean isTokenValid = validateToken(token);
		log.info("Token validity: {}", isTokenValid);

		if (isTokenValid) {
			Set<String> hkset = hashOps.keys(token);
			log.info("Retrieved keys from Redis for token {}: {}", token, hkset);
			List<GrantedAuthority> roles = new ArrayList<>();

			for (String randomHash : hkset) {
				String wso2UserData = hashOps.get(token, randomHash);
				log.info("Retrieved user data for key {}: {}", randomHash, wso2UserData);
				try {
					WSO2User wso2User = mapper.readValue(wso2UserData, WSO2User.class);
					List<Role> rolesList = wso2User.getRoles();
					
					for (Role role : rolesList) {
	                    if (!"everyone".equals(role.getDisplay())) {
	                        String userRole = role.getDisplay().replace("Application/", "");
	                        log.info("Provisioned user role:\t" + userRole);
	                        roles.add(new SimpleGrantedAuthority(userRole));
	                    } else {
	                        log.info("Skipping the Internal/everyone role");
	                    }
	                }
					String userName = wso2User.getUserName();

					authn = new UsernamePasswordAuthenticationToken(userName, null, roles);

				} catch (JsonMappingException e) {
					log.error("Error mapping JSON: {}", e.getMessage());
				} catch (JsonProcessingException e) {
					log.error("Error processing JSON: {}", e.getMessage());
				}
			}
		}
		return authn;
	}

	private boolean validateToken(String token) {
		try {
			if (hashOps.hasKey(JWT_TOKEN_EXPIRY_KEY, token)) {

				String expiryInSeconds = hashOps.get(JWT_TOKEN_EXPIRY_KEY, token);
				long tokenExpiryInSeconds = Long.valueOf(expiryInSeconds);
				log.info("Token expiry from Redis: {}", tokenExpiryInSeconds);

				if (expiryDate(tokenExpiryInSeconds).before(new Date())) {
					return false;
				}
				return true;
			} else {
				throw new RuntimeException("Token is Invalid!! Please Login Again");
			}

		} catch (Exception e) {
			log.error("Error validating token: {}", e.getMessage());
			return false;
		}

	}

	private String resolveToken(HttpServletRequest request) {
		String headerValue = request.getHeader(JWT_TOKEN_HEADER_NAME);
		headerValue = headerValue.replace(JWT_TOKEN_TYPE, "");
		log.info("jwt token is {}", headerValue);
		return headerValue;
	}

	public Date expiryDate(long tokenExpiry) {
		Date date = new Date();
		System.out.println(date);
		long time = date.getTime() + tokenExpiry * 1000;
		Date updatedDate = new Date(time);
		return updatedDate;
	}
}
