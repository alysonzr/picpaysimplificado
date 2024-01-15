package com.picpaysimplificado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.NotificationDTO;

@Service
public class NotificationService {

	@Autowired
	private RestTemplate restTemplate;
	
	public void sendNotification(User user, String message) throws Exception {
		NotificationDTO notificationRequest = new NotificationDTO(user.getEmail(), message);
		String url = "https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6";
		ResponseEntity<String> notificationReponse = restTemplate.postForEntity(url, notificationRequest, String.class);
		
		if(notificationReponse.getStatusCode() != HttpStatus.OK) 
			throw new Exception("Serviço de notificaçao esta fora do ar");
		
		System.out.println("notificaçao Enviada");
		
	}
}
