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

import io.swagger.v3.core.util.Json;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAuthnServiceImpl implements UserAuthnService {

	private IamAuthnCommunicator authnComm;

	private RedisTemplate<String, String> redisTemplate;
	HashOperations<String, String, String> hashOps = null;

	public UserAuthnServiceImpl(IamAuthnCommunicator authnComm, RedisTemplate<String, String> redisTemplate) {
		this.authnComm = authnComm;
		this.redisTemplate = redisTemplate;
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
		if (StringUtils.isNotEmpty(authnResponse)) {
			String randomHash = authnBean.getUserName() + "_" + RandomStringUtils.random(512);
			log.info("token hash is {}" + randomHash);
			hashOps.put(authnBean.getUserName(), randomHash, access_token);
			hashOps.put(access_token, randomHash, authnBean.getUserName());
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
				String userName = hashOps.get(token, randomHash);
				log.info("User Name to be log out is:\t" + userName);
				hashOps.delete(userName, randomHash);
				hashOps.delete(token, randomHash);
				String logoutMessage = "user " + userName + " have been logout successfully";
				json.put("message", logoutMessage);
			});
		}

		return json.toString();
	}

}
