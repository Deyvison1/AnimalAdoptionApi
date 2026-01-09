package com.animaladoption.api.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class NotPublishException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	private final int status;

    public NotPublishException(int status, String message) {
        super(message);
        this.status = status;
    }
}
