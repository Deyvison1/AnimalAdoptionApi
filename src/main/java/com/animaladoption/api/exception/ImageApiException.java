package com.animaladoption.api.exception;

import lombok.Getter;

@Getter
public class ImageApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final int status;

    public ImageApiException(int status, String message) {
        super(message);
        this.status = status;
    }
}
