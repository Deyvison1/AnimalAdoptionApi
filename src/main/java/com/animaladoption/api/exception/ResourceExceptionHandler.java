package com.animaladoption.api.exception;

import java.sql.SQLException;
import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.animaladoption.api.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			HttpServletRequest request) {
		String message = "Violação de integridade de dados.";

		Throwable rootCause = ex.getRootCause();
		if (rootCause instanceof SQLException sqlEx) {
			String msg = sqlEx.getMessage().toLowerCase();

			if (msg.contains("unique") || msg.contains("duplicate")) {
				message = "Já existe uma especialidade com este nome para este barbeiro.";
			} else if (msg.contains("key constraint")) {
				message = "Esse registro nao pode ser excluido. Tem dependencias dele mapeadas no banco.";
			}
		}

		return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message, String path) {
		ErrorResponseDTO error = new ErrorResponseDTO(Instant.now(), status.value(), status.getReasonPhrase(), message,
				path);
		return ResponseEntity.status(status).body(error);
	}
}
