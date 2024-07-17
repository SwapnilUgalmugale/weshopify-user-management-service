package com.weshopify.platform.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtAuthenticationService {

	private RedisTemplate<String, String> redisTemplate;
	HashOperations<String, String, String> hashOps = null;

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
		Authentication authn =   null;
		String token = resolveToken(request);
		boolean isTokenValid = validateToken(token);
		if (isTokenValid) {
			
			String userRoles = hashOps.get(USER_ROLES_KEY, token);
			log.info("User Roles from Redis: {}",userRoles);
			List<GrantedAuthority> roles = new ArrayList<>();
			roles.add(new SimpleGrantedAuthority(userRoles));
			
			String userName = hashOps.get(USER_SUBJECT_NAME, token);
			log.info("User name from Redis: {}", userName);
			
			authn = new UsernamePasswordAuthenticationToken(roles, userName, null);	
		}
		return authn;
	}

	private boolean validateToken(String token) {
		try {
			if (hashOps.hasKey(JWT_TOKEN_EXPIRY_KEY, token)) {
				
				String expiryInSeconds = hashOps.get(JWT_TOKEN_EXPIRY_KEY, token);
				long tokenExpiryInSeconds = Long.valueOf(expiryInSeconds);
				log.info("Token expiry from Redis: {}",tokenExpiryInSeconds);
				
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
		long time = date.getTime() + tokenExpiry*1000;
		Date updatedDate = new Date(time);
		return updatedDate;
	}
}
