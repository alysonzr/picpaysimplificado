package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	public void validateTransation(User sender, BigDecimal amount) throws Exception {
		if(sender.getUserType() == UserType.MERCHANT) 
			throw new Exception("Usuario do tipo Lojista não está autorizado a realizar transação");
		
		if(sender.getBalance().compareTo(amount) < 0) 
			throw new Exception("Usuario não possui saldo para realizar essa transação");
	}
	
	public User findUserById(Long id) throws Exception {
		return this.repository.findById(id).orElseThrow(() -> new Exception("Usuario não Encontrado"));
	}
	
	public void saveUser(User user) {
		this.repository.save(user);
	}
	

	public User createUser(UserDTO userDTO) {
		User user = new User(userDTO);
		saveUser(user);
		return user;
	}

	public List<User> getAllUseres(){
		return repository.findAll();
	}
}
