package com.picpaysimplificado.services;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	private AuthorizationService authorizationService;
	
	@Autowired
	private NotificationService notificationService;
	
	public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
		User sender = this.userservice.findUserById(transactionDTO.senderId());
		User receiver = this.userservice.findUserById(transactionDTO.receiverId());
		
		userservice.validateTransation(sender, transactionDTO.value());
		
		boolean isAuthorized = this.authorizationService.authorizeTransaction(sender, transactionDTO.value());
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

}
