package com.animaladoption.api.exception;

import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.animaladoption.api.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
	}
	
	@ExceptionHandler(NotPublishException.class)
	public ResponseEntity<ErrorResponseDTO> handleBusinessRuleException(
			NotPublishException ex,
	        HttpServletRequest request) {

	    HttpStatus status = HttpStatus.resolve(ex.getStatus());
	    if (status == null) {
	        status = HttpStatus.BAD_REQUEST;
	    }

	    return buildResponse(status, ex.getMessage(), request.getRequestURI());
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
				message = "Esse registro não pode ser excluído. Existem dependências relacionadas no banco.";
			}
		}

		return buildResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	@ExceptionHandler(FileAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDTO> handleFileAlreadyExists(FileAlreadyExistsException ex,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex,
			HttpServletRequest request) {
		String message = ex.getReason() != null ? ex.getReason() : ex.getMessage();
		return buildResponse(ex.getStatusCode(), message, request.getRequestURI());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ImageApiException.class)
	public ResponseEntity<ErrorResponseDTO> handleImageApiException(ImageApiException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.resolve(ex.getStatus());
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return buildResponse(status, ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(Instant.now(), HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN.getReasonPhrase(),
				"Acesso negado: você não tem permissão para acessar esse recurso.", request.getRequestURI());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}

	/**
	 * Ajuste principal: HttpStatusCode aceita tanto HttpStatus quanto códigos
	 * genéricos
	 */
	private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatusCode status, String message, String path) {
		ErrorResponseDTO error = new ErrorResponseDTO(Instant.now(), status.value(), status.toString(),
				message, path);
		return ResponseEntity.status(status.value()).body(error);
	}
}
