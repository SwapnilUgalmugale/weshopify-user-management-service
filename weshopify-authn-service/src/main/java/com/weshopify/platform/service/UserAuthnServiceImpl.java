package com.weshopify.platform.service;

import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.weshopify.platform.bean.UserAuthnBean;
import com.weshopify.platform.model.WSO2UserAuthnBean;
import com.weshopify.platform.outbound.IamAuthnCommunicator;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAuthnServiceImpl implements UserAuthnService {

	private IamAuthnCommunicator authnComm;

	HashOperations<String, String, String> hashOps = null;

	public UserAuthnServiceImpl(IamAuthnCommunicator authnComm, RedisTemplate<String, String> redisTemplate) {
		this.authnComm = authnComm;
		this.hashOps = redisTemplate.opsForHash();
	}

	@Override
	public String authenticate(UserAuthnBean authnBean) {

		WSO2UserAuthnBean wso2AuthnBean = WSO2UserAuthnBean.builder().username(authnBean.getUserName())
				.password(authnBean.getPassword()).build();
		
		String authnResponse = authnComm.authenticate(wso2AuthnBean);
		log.info("authentication response is {}", authnResponse);
		JSONObject json = new JSONObject(authnResponse);
		String access_token = json.getString("access_token"); 
		log.info("access token is:\t" + access_token);
		
		int expiry = json.getInt("expires_in");
		if (StringUtils.isNotEmpty(authnResponse)) {
			String randomHash = authnBean.getUserName() + "_" + RandomStringUtils.random(512);
			log.info("token hash is {}" + randomHash);
			/*
			 * hashOps.put(authnBean.getUserName(), randomHash, access_token);
			 * hashOps.put("SUBJECT",access_token, authnBean.getUserName());
			 */
			hashOps.put("tokenExpiry", access_token, String.valueOf(expiry));
			
			String wso2UserData = authnComm.getUserProfile(access_token);
			hashOps.put(access_token,randomHash, wso2UserData);
		}
		return authnResponse;
	}

	@Override
	public String logout(String tokenType, String token) {
		Set<String> hkset = hashOps.keys(token);
		String logoutResp = authnComm.logout(tokenType, token);
		JSONObject json = new JSONObject();
		if (StringUtils.isNotEmpty(logoutResp)) {
			hkset.stream().forEach(randomHash -> {
//				String wso2User = hashOps.get(token, randomHash);
//				log.info("User Name to be log out is:\t" + wso2User.getUserName());
				hashOps.delete(token, randomHash);
//				hashOps.delete("SUBJECT", token);
				hashOps.delete("tokenExpiry", token);
//				hashOps.delete("USER_ROLES", token);
				String logoutMessage = "user " +""+ " have been logout successfully";
				json.put("message", logoutMessage);
			});
		}

		return json.toString();
	}

}
