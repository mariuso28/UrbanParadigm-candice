package org.cz.rest.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.cz.json.message.CzResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/a")
public class AuthRestController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AuthRestController.class);

	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/authorize", method=RequestMethod.POST)
	public CzResultJson authorize(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request
			){
		CzResultJson result = new CzResultJson();
		String path;
		path = "http://localhost:8080/candice/oauth/token";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "password");
			map.add("client_id", "barClientIdPassword");
			map.add("client_secret", "secret");
			map.add("username", username);
			map.add("password", password);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.POST, entity, Object.class);
			result.success(responseEntity.getBody());
		}
		catch(HttpClientErrorException e){
			e.printStackTrace();
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				result.fail("username or password incorrect");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/refreshToken", method=RequestMethod.POST)
	public CzResultJson refreshToken(
			@RequestParam("refreshToken") String refreshToken,
			HttpServletRequest request
			){
		CzResultJson result = new CzResultJson();
		String path;
		path = "http://localhost:8080/candice/oauth/token";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "refresh_token");
			map.add("refresh_token", refreshToken);
			map.add("client_id", "barClientIdPassword");
			map.add("client_secret", "secret");
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.POST, entity, Object.class);
			result.success(responseEntity.getBody());
		}
		catch(Exception e){
			e.printStackTrace();
			result.fail("Bad credentials");
		}
		return result;
	}

	@RequestMapping(value="/revokeRefreshToken", method=RequestMethod.POST)
	public CzResultJson revokeRefreshToken(
			@RequestParam("refreshToken") String refreshToken
			){
		CzResultJson result = new CzResultJson();
		((JdbcTokenStore) tokenStore).removeAccessTokenUsingRefreshToken(refreshToken);
		((JdbcTokenStore) tokenStore).removeRefreshToken(refreshToken);
		result.success();
		return result;
	}
}