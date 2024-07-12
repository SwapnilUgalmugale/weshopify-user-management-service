package com.weshopify.platform.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weshopify.platform.bean.RoleBean;
import com.weshopify.platform.utils.ApplicationsUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoleMgmtClient {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ApplicationsUtil porpsUtil;

	@Value("${iam-server.base-url}")
	private String baseUrl;

	@Value("${iam-server.role-api}")
	private String roleApi;

	@Value("${iam-server.role-api-schema}")
	private String roleApiSchema;
	
	/*
	 * @Value("${iam-server.user-name}") // private String iam_user_name;
	 * 
	 * @Value("${iam-server.password}") // private String iam_password;
	 * 
	 * public void findAllRoles() { // String url = baseUrl + roleApi; // String
	 * response = restTemplate.getForObject(url, String.class); //
	 * System.out.println(response); // }
	 */

	public List<RoleBean> findAllRoles() {
		List<RoleBean> resourcesList = null;
		String role_api_url = baseUrl + roleApi;
		log.info("constructed role api url is :\t" + role_api_url);

		HttpEntity<String> requestBody = porpsUtil.preparedRequestBody(null);

		ResponseEntity<Object> apiResponse = restTemplate.exchange(role_api_url, HttpMethod.GET, requestBody,
				Object.class);
		log.info("response code of the role api is:\t" + apiResponse.getStatusCode().value());
		if (HttpStatus.OK.value() == apiResponse.getStatusCode().value()) {
			Object responseBody = apiResponse.getBody();
			resourcesList = porpsUtil.parseRoleResponse(responseBody);
		}
		return Optional.ofNullable(resourcesList).get();

	}

	public List<RoleBean> createRole(RoleBean roleBean) {
		List<RoleBean> resourcesList = null;
		String role_api_url = baseUrl + roleApi;
		log.info("constructed role api url is :\t" + role_api_url);

		// roleBean.setSchemas(new String[]
		// {"urn:itef:params:scim:schemas:extention:2.0:Role"});
		// roleBean.setSchemas(new String[]
		// {"urn:ietf:params:scim:schemas:core:2.0:Role"});
		
		
		roleBean.setSchemas(new String[] { roleApiSchema });

		String rolePayload = null;
		try {
			rolePayload = objectMapper.writeValueAsString(roleBean);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String rolePayload = objectMapper.convertValue(roleBean, String.class);
		HttpEntity<String> requestBody = porpsUtil.preparedRequestBody(rolePayload);

		ResponseEntity<Object> apiResponse = restTemplate.exchange(role_api_url, HttpMethod.POST, requestBody,
				Object.class);
		log.info("response code of the role api is:\t" + apiResponse.getStatusCode().value());
		if (HttpStatus.CREATED.value() == apiResponse.getStatusCode().value()) {

			// Object responseBody = apiResponse.getBody();

			resourcesList = findAllRoles();
		}
		return Optional.ofNullable(resourcesList).get();

	}

	public boolean deleteRoleById(String roleId) {

		String delteRoleApiUrl = baseUrl + roleApi + "/" + roleId;
		log.info("constructed role delete api url is:\t" + delteRoleApiUrl);

		HttpEntity<String> requestBody = porpsUtil.preparedRequestBody(null);
		ResponseEntity<Object> apiResponse = restTemplate.exchange(delteRoleApiUrl, HttpMethod.DELETE, requestBody,
				Object.class);
		log.info("response code of the delete role api is:\t" + apiResponse.getStatusCode().value());

		return HttpStatus.NO_CONTENT.value() == apiResponse.getStatusCode().value();
	}

	public List<RoleBean> updateRole(RoleBean roleBean) {
		List<RoleBean> resourcesList = null;
		String updateRoleApuUrl = baseUrl + roleApi + "/" + roleBean;
		log.info("constructed role delete api url is:\t" + updateRoleApuUrl);

		String rolePayload = null;
		try {
			rolePayload = objectMapper.writeValueAsString(roleBean);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String rolePayload = objectMapper.convertValue(roleBean, String.class);
		HttpEntity<String> requestBody = porpsUtil.preparedRequestBody(rolePayload);

		ResponseEntity<Object> apiResponse = restTemplate.exchange(updateRoleApuUrl, HttpMethod.PUT, requestBody,
				Object.class);
		log.info("response code of the role api is:\t" + apiResponse.getStatusCode().value());

		return Optional.ofNullable(resourcesList).get();
	}

	/*
	 * private HttpEntity<String> preparedRequestBody(String rolePayload) {
	 * HttpEntity<String> requestBody = null;
	 * 
	 * String role_api_url = baseUrl + roleApi; String res =
	 * restTemplate.getForObject(role_api_url, String.class);
	 * System.out.println(res);
	 * 
	 * log.info("constructed role api url is :\t" + role_api_url);
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	 * headers.setContentType(MediaType.APPLICATION_JSON);
	 * 
	 * // HttpEntity<String> entity = new HttpEntity<>(headers);
	 * 
	 * try { if (StringUtils.isEmpty(rolePayload) ||
	 * StringUtils.isBlank(rolePayload)) { // res = sanitizeRolePayload(res);
	 * rolePayload = urlEncode(rolePayload); requestBody = new
	 * HttpEntity<String>(res, headers);
	 * 
	 * } else { res = urlEncode(res); // rolePayload =
	 * sanitizeRolePayload(rolePayload); requestBody = new
	 * HttpEntity<String>(rolePayload, headers);
	 * 
	 * } } catch (UnsupportedEncodingException e) {
	 * log.error("Error encoding role payload", e); }
	 * 
	 * return requestBody;
	 * 
	 * }
	 */

	/*
	 * private String urlEncode(String value) throws UnsupportedEncodingException {
	 * return value == null ? null : URLEncoder.encode(value, "UTF-8"); }
	 * 
	 * 
	 * private String sanitizeRolePayload(String rolePayload) { // Example
	 * sanitization: remove or replace invalid characters // Replace "/" with an
	 * empty string or any other valid character return rolePayload.replaceAll("/",
	 * ""); }
	 */

	/*
	 * private List<RoleBean> parseRoleResponse(Object responseBody) {
	 * List<RoleBean> resourceList = null; try { String response =
	 * objectMapper.writeValueAsString(responseBody);
	 * log.info("iam role api response is:\t" + response);
	 * 
	 * JSONObject jsonResponseObject = new JSONObject(response);
	 * 
	 * JSONArray jasonArray = (JSONArray) Optional.ofNullable(jsonResponseObject)
	 * .filter(condition ->
	 * jsonResponseObject.has("Resources")).get().get("Resources");
	 * log.info("Resources are:\t" + jasonArray.toString());
	 * 
	 * Gson gson = new Gson(); Type type = new TypeToken<List<RoleBean>>() {
	 * }.getType(); resourceList = gson.fromJson(jasonArray.toString(), type);
	 * log.info("Resources list are:\t" + resourceList.size());
	 * 
	 * return resourceList; } catch (JsonProcessingException e) {
	 * log.error(e.getMessage()); } return Optional.ofNullable(resourceList).get();
	 * 
	 * }
	 */

}
