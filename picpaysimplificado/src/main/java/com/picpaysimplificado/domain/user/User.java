package com.picpaysimplificado.domain.user;
import java.math.BigDecimal;

import com.picpaysimplificado.dtos.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity(name="users")
@Table(name="users")
@Getter 
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@SuppressWarnings("unused")
	private String firstName;
	
	private String lastName;
	
	@Column(unique = true)
	private String document;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private BigDecimal balance;
	
	@Enumerated(EnumType.STRING)
	private UserType userType;

	public User() {
		super();
	}
	
	public User(UserDTO userDTO) {
		this.firstName = userDTO.firstName();
		this.lastName = userDTO.lastName();
		this.document = userDTO.document();
		this.email = userDTO.email();
		this.password = userDTO.password();
		this.balance = userDTO.balance();
		this.userType = userDTO.userType();
		
	}


	

	


}
