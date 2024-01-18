package com.picpaysimplificado.services;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.repositories.TransactionRepository;

class TransactionServiceTest {

	@Mock
	private UserService userservice;
	
	@Mock 
	private TransactionRepository repository; 
	
	@Mock
	private AuthorizationService authorizationService;
	
	@Mock
	private NotificationService notificationService;
	
	@Autowired
	@InjectMocks
	private TransactionService transactionService;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("deve criar a transação com sucesso quando tudo estiver OK ")
	void testCreateTransactionCase1() throws Exception {
		User sender = new User(1L,"Alyson", "Zancananaro", "999999901", "alyson@gmail.com","9999922",new BigDecimal(10), UserType.COMMON);
		User receiver = new User(2L,"Amanda", "Bartz", "999999902", "aamanda@gmail.com","9999922",new BigDecimal(10), UserType.COMMON);
		
		when(userservice.findUserById(1L)).thenReturn(sender);
		when(userservice.findUserById(2L)).thenReturn(receiver);
		
		when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);
		
		TransactionDTO request = new TransactionDTO(1L, 2L, new BigDecimal(10));
		transactionService.createTransaction(request);
		
		verify(repository, times(1)).save(any());
		
		sender.setBalance(new BigDecimal(0));
		verify(userservice, times(1)).saveUser(sender);
		
		receiver.setBalance(new BigDecimal(20));
		verify(userservice, times(1)).saveUser(receiver);
		
		verify(notificationService, times(1)).sendNotification(sender, "Transação Realizada Com Sucesso!" );
		verify(notificationService, times(1)).sendNotification(receiver, "Transação Recebida Com Sucesso!" );
		
	}

	@Test
	@DisplayName("deve criar uma execao quando a Transaçao nao estiver autorizada ")
	void testCreateTransactionCase2() throws Exception {
		User sender = new User(1L,"Alyson", "Zancananaro", "999999901", "alyson@gmail.com","9999922",new BigDecimal(10), UserType.COMMON);
		User receiver = new User(2L,"Amanda", "Bartz", "999999902", "aamanda@gmail.com","9999922",new BigDecimal(10), UserType.COMMON);
		
		when(userservice.findUserById(1L)).thenReturn(sender);
		when(userservice.findUserById(2L)).thenReturn(receiver);
		
		when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);
		
		Exception thrown = Assertions.assertThrows(Exception.class, () -> {
			TransactionDTO request = new TransactionDTO(1L, 2L, new BigDecimal(10));
			transactionService.createTransaction(request);
		});
		
		Assertions.assertEquals("Transação Não Autorizada", thrown.getMessage());
		
		
		

	}
}
