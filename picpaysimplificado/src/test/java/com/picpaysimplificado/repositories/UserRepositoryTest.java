package com.picpaysimplificado.repositories;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.UserDTO;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	@DisplayName("DEVE OBTER O USUÁRIO COM SUCESSO DO DB")
	void findUserByDocumentCase1() {
		String document = "999999901";
		UserDTO data = new UserDTO("Alyson", "Zancanaro", document, new BigDecimal(10), "alyson@gmail.com", "59353434", UserType.COMMON);
		createUser(data);
		
		Optional<User> result = userRepository.findUserByDocument(document);
		
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("NÃO DEVE OBTER O USUÁRIO DO DB QUANDO O USUÁRIO NÃO EXISTE")
	void findUserByDocumentCase2() {
		String document = "999999901";
		
		Optional<User> result = userRepository.findUserByDocument(document);
		
		assertThat(result.isEmpty()).isTrue();
	}
	
	private User createUser(UserDTO data) {
		User user = new User(data);
		entityManager.persist(user);
		return user;
	}
}
