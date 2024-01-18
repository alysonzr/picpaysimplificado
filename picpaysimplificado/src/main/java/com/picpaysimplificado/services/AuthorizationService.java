package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.domain.user.User;

@Service
public class AuthorizationService {

	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("rawtypes")
	public boolean authorizeTransaction(User sender, BigDecimal value) {
		String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";
		
		ResponseEntity<Map> authorizationReponse = restTemplate.getForEntity(url, Map.class);
		if(authorizationReponse.getStatusCode() == HttpStatus.OK) {
			Map body =  authorizationReponse.getBody();
		     if (body != null && body.containsKey("message")) {
		    	 System.out.println(body.get("message").equals("Autorizado"));
		    	 return body.get("message").equals("Autorizado");
		     }
		}
		return false;

	}
}
