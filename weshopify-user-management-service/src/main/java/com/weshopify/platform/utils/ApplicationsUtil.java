package com.weshopify.platform.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weshopify.platform.Dto.WSO2User;
import com.weshopify.platform.bean.RoleBean;

import lombok.Data;


@Component
@Data

public class ApplicationsUtil {

	private static final Logger log = LoggerFactory.getLogger(ApplicationsUtil.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${iam-server.base-url}")
	private String baseUrl;

	@Value("${iam-server.role-api}") // Roles
	private String roleApi;

	@Value("${iam-server.user-api}")// Users
	private String userApi;

	@Value("${iam-server.role-api-schema}")
	private String roleApiSchema;

	public HttpEntity<String> preparedRequestBody(String rolePayload) {
		HttpEntity<String> requestBody = null;

		/*
		 * String role_api_url = baseUrl + roleApi; String res =
		 * restTemplate.getForObject(role_api_url, String.class);
		 * log.info("constructed role api url is :\t" + role_api_url);
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

//		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			if (StringUtils.isEmpty(rolePayload) || StringUtils.isBlank(rolePayload)) {
				// res = sanitizeRolePayload(res);
				rolePayload = urlEncode(rolePayload);
				requestBody = new HttpEntity<String>(headers);

			} else {
				// res = urlEncode(res);
				// rolePayload = sanitizeRolePayload(rolePayload);
				requestBody = new HttpEntity<String>(rolePayload, headers);

			}
		} catch (UnsupportedEncodingException e) {
			log.error("Error encoding role payload", e);
		}

		return requestBody;

	}

	public String urlEncode(String value) throws UnsupportedEncodingException {
		return value == null ? null : URLEncoder.encode(value, "UTF-8");
	}

	public List<RoleBean> parseRoleResponse(Object responseBody) {
		List<RoleBean> resourceList = null;
		try {
			String response = objectMapper.writeValueAsString(responseBody);
			log.info("iam role api response is:\t" + response);

			JSONObject jsonResponseObject = new JSONObject(response);

			JSONArray jasonArray = (JSONArray) Optional.ofNullable(jsonResponseObject)
					.filter(condition -> jsonResponseObject.has("Resources")).get().get("Resources");
			log.info("Resources are:\t" + jasonArray.toString());

			Gson gson = new Gson();
			Type type = new TypeToken<List<RoleBean>>() {
			}.getType();
			resourceList = gson.fromJson(jasonArray.toString(), type);
			log.info("Resources list are:\t" + resourceList.size());

			return resourceList;
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		return Optional.ofNullable(resourceList).get();

	}

	public List<WSO2User> parseUserResponse(Object responseBody) {
		List<WSO2User> resourceList = null;
		try {

			String response = objectMapper.writeValueAsString(responseBody);
			log.info("iam user api response is:\t" + response);

			JSONObject jsonResponseObject = new JSONObject(response);

			JSONArray jasonArray = (JSONArray) Optional.ofNullable(jsonResponseObject)
					.filter(condition -> jsonResponseObject.has("Resources"))
					.map(condition -> jsonResponseObject.getJSONArray("Resources"))
					.orElseThrow(() -> new IllegalArgumentException("No 'Resources' field in JSON response"));
			log.info("Resources are:\t" + jasonArray.toString());

			Gson gson = new Gson();
			Type type = new TypeToken<List<WSO2User>>() {}.getType();
			resourceList = gson.fromJson(jasonArray.toString(), type);
			log.info("Resources list are:\t" + resourceList.size());

			return resourceList;
		} catch (JsonProcessingException e) {
			log.error("Error processing JSON: "+e.getMessage());
		}catch (Exception e) {
			log.error("Error parsing user response: "+e.getMessage());
		}
		return Optional.ofNullable(resourceList).orElseThrow(()-> new IllegalStateException("Resource list is null"));

	}

}
