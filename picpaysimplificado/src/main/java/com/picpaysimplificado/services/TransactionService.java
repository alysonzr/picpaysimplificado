package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private UserService userservice;
	
	@Autowired 
	private TransactionRepository repository; 
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private NotificationService notificationService;
	
	public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
		User sender = this.userservice.findUserById(transactionDTO.senderId());
		User receiver = this.userservice.findUserById(transactionDTO.receiverId());
		
		userservice.validateTransation(sender, transactionDTO.value());
		
		boolean isAuthorized = this.authorizeTransaction(sender, transactionDTO.value());
		if(!isAuthorized) 
			throw new Exception("Transação Não Autorizada");
		
		Transaction transaction = new Transaction();
		transaction.setAmount(transactionDTO.value());
		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setTimestamp(LocalDateTime.now());
		
		sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
		receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));
		
		repository.save(transaction);
		userservice.saveUser(sender);
		userservice.saveUser(receiver);

		notificationService.sendNotification(sender, "Transação Realizada Com Sucesso!");
		notificationService.sendNotification(receiver, "Transação Recebida Com Sucesso!");
		
		return transaction;
	}
	
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
