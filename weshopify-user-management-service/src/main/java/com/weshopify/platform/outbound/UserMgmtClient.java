package com.weshopify.platform.outbound;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weshopify.platform.Dto.WSO2User;
import com.weshopify.platform.exceptions.APIException;

import com.weshopify.platform.utils.ApplicationsUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserMgmtClient {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ApplicationsUtil propsUtil;

	public List<WSO2User> findAllUsers() {

		try {
			List<WSO2User> wso2UsersList = null;
			String user_api_url = propsUtil.getBaseUrl() + propsUtil.getUserApi();
			// log.info("User api url is:\t"+user_api_url);
			log.info("user api url is", user_api_url);

			HttpEntity<String> requestBody = propsUtil.preparedRequestBody(null);

			ResponseEntity<Object> apiResponse = restTemplate.exchange(user_api_url, HttpMethod.GET, requestBody,
					Object.class);
			log.info("response code of the role api is:\t" + apiResponse.getStatusCode().value());

			if (HttpStatus.OK.value() == apiResponse.getStatusCode().value()) {
				Object responseBody = apiResponse.getBody();
				wso2UsersList = propsUtil.parseUserResponse(responseBody);
			}else {
			 throw new APIException(apiResponse.getBody().toString(), apiResponse.getStatusCode().value());
			}
			return Optional.ofNullable(wso2UsersList).orElseThrow(() -> new IllegalStateException("No Users Found"));

		} catch (Exception e) {
			throw new APIException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	public List<WSO2User> createUser(WSO2User wso2User) {

		try {
			List<WSO2User> wso2UsersList = null;
			String user_api_url = propsUtil.getBaseUrl() + propsUtil.getUserApi();
			log.info("user api url is", user_api_url);

			String rolePayload = null;
			rolePayload = objectMapper.writeValueAsString(wso2User);

			HttpEntity<String> requestBody = propsUtil.preparedRequestBody(rolePayload);
			ResponseEntity<Object> apiResponse = restTemplate.exchange(user_api_url, HttpMethod.POST, requestBody,
					Object.class);
			log.info("response code of user api:\t" + apiResponse.getStatusCode().value());

			if (HttpStatus.CREATED.value() == apiResponse.getStatusCode().value()) {

				wso2UsersList = findAllUsers();
			} else {
				throw new APIException(apiResponse.getBody().toString(), apiResponse.getStatusCode().value());
			}

			return Optional.ofNullable(wso2UsersList).get();

		} catch (Exception e) {
			throw new APIException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value());
		}
	}
}
