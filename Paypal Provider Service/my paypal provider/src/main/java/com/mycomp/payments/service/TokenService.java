package com.mycomp.payments.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mycomp.payments.constant.Constant;
import com.mycomp.payments.http.HttpRequest;
import com.mycomp.payments.http.HttpServiceEngine;
import com.mycomp.payments.paypal.res.PaypalOAuthToken;
import com.mycomp.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {
	
	private static final int REDIS_ACCESS_TOKEN_EXPIRY_DIFF = 60;

	private static final String PAYPAL_ACCESS_TOKEN = "PAYPAL_ACCESS_TOKEN";

	private final HttpServiceEngine httpServiceEngine;
	
	@Value("${paypal.client.id}")
	private String clientId;
	
	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.oauth.url}")  
	private String outhUrl;
	
	private final JsonUtil jsonUtil;
	
	private final RedisService redisService;
	
	/**
	 * 
	 * @return access token string
	 */
	public String getAccessToken() {
		log.info("Retrieving access token from TokenService");
		
		String accessToken = redisService.getValue(PAYPAL_ACCESS_TOKEN);
		log.info("Access token from Redis: {}", accessToken);
		
		if (accessToken != null) {
			log.info("Returning cached access token");
			return accessToken;
		}
		
		log.info("No cached access token found, calling OAuth service");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(clientId, clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add(Constant.GRANT_TYPE, Constant.CLIENT_CREDENTIALS);

		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.POST);
		httpRequest.setUrl(outhUrl);
		httpRequest.setHttpHeaders(headers);
		httpRequest.setBody(formData);
		
		log.info("Prepared HttpRequest for OAuth call: {}", httpRequest);
		
		ResponseEntity<String> response = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("HTTP response from HttpServiceEngine: {}", response);
		
		String tokenBody = response.getBody();
		
		PaypalOAuthToken token = jsonUtil.fromJson(
				tokenBody, PaypalOAuthToken.class);
		
		accessToken = token.getAccessToken();
		
		// Cache the access token in Redis with expiry time
		redisService.setValueWithExpiry(
				PAYPAL_ACCESS_TOKEN, 
				accessToken, 
				token.getExpiresIn() - REDIS_ACCESS_TOKEN_EXPIRY_DIFF); // subtracting 60 seconds as buffer
		
		log.info("Caching access token for future use");
		
		return accessToken;
	}

}
