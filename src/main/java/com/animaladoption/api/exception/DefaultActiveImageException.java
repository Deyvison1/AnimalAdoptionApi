package com.animaladoption.api.exception;

import java.io.Serial;

public class DefaultActiveImageException extends RuntimeException {
	@Serial
    private static final long serialVersionUID = 1L;

	public DefaultActiveImageException(String message) {
		super(message);
	}
}
