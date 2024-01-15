package com.picpaysimplificado.dtos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public record TransactionDTO(Long senderId, Long receiverId,BigDecimal value) {
	
	

}
