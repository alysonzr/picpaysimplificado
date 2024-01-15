package com.picpaysimplificado.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.picpaysimplificado.dtos.ExceptionDTO;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity threatDuplicateEntry(DataIntegrityViolationException exeption) {
		ExceptionDTO exceptionDTO = new ExceptionDTO("Usuario já cadastrado", "400");
		return ResponseEntity.badRequest().body(exceptionDTO);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity threat404(EntityNotFoundException exeption) {
		return ResponseEntity.notFound().build();
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity threatGeneralException(Exception exeption) {
		ExceptionDTO exceptionDTO = new ExceptionDTO(exeption.getMessage(), "500");
		return ResponseEntity.internalServerError().body(exceptionDTO);
	}
}
