package com.weshopify.platform.outbound;

import java.util.Base64;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weshopify.platform.model.WSO2UserAuthnBean;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IamAuthnCommunicator {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${weshopify-platform.oauth2.grant_type}")
	private String grant_type;

	@JsonIgnore
	@Value("${weshopify-platform.oauth2.clientId}")
	private String ClientId;

	@JsonIgnore
	@Value("${weshopify-platform.oauth2.clientSecrete}")
	private String ClientSecrete;

	@JsonIgnore
	@Value("${weshopify-platform.oauth2.uri}")
	private String tokenUrl;

	@Value("${weshopify-platform.oauth2.scope}")
	private String scope;

	@Value("${weshopify-platform.oauth2.logoutUri}")
	private String logoutUri;

	public String authenticate(WSO2UserAuthnBean authnBean) {
		log.info("uri is:\t" + authnBean);
		String respData = null;
		try {
			authnBean.setGrant_type(grant_type);
			authnBean.setScope(scope);

			String payload = objectMapper.writeValueAsString(authnBean);
			
			HttpHeaders headers = basicAuthHeader();
			HttpEntity<String> request = preparedJsonRequestBody(headers,payload);

			ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
			log.info("status code is:\t" + response.getStatusCode().value());

			if (HttpStatus.OK.value() == response.getStatusCode().value()) {
				respData = response.getBody();
				log.info("response body is:\t" + respData);
			}
		} catch (JsonProcessingException e) {
			log.info("JSON processing error: ", e);
			e.printStackTrace();
		} catch (Exception e) {
			log.info("Authentication error: ", e);
		}

		return Optional.ofNullable(respData).get();
	}

	public String logout(String tokenType, String token) {
		log.info("uri is:\t" + logoutUri);
		String respData = null;
		try {
			
			String payload = "token_type_hint="+tokenType+"&token="+token;

			HttpHeaders headers = basicAuthHeader();
			HttpEntity<String> request = preparedFormRequestBody(headers,payload);

			ResponseEntity<String> response = restTemplate.exchange(logoutUri, HttpMethod.POST, request, String.class);
			log.info("status code is:\t" + response.getStatusCode().value());

			if (HttpStatus.OK.value() == response.getStatusCode().value()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("message", "User has been logged out successfully");
				respData = jsonObject.toString();
				log.info("response body is:\t" + respData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(respData).get();

	}

	private HttpHeaders basicAuthHeader() {
		String adminCreds = ClientId + ":" + ClientSecrete;
		log.info("admin creds are:\t" + adminCreds);

		String encodedAdminCreds = Base64.getEncoder().encodeToString(adminCreds.getBytes());
		log.info("admin creds are:\t" + encodedAdminCreds);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + encodedAdminCreds);

		return headers;

	}

	private HttpEntity<String> preparedJsonRequestBody(HttpHeaders headers, String payload) {

		headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<String> requestBody = new HttpEntity<String>(payload, headers);
		return requestBody;

	}
	private HttpEntity<String> preparedFormRequestBody(HttpHeaders headers, String payload) {

		headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		HttpEntity<String> requestBody = new HttpEntity<String>(payload, headers);
		return requestBody;

	}

}
