package com.animaladoption.api.exception;

import lombok.Getter;

@Getter
public class ImageApiException extends RuntimeException {
    private final int status;

    public ImageApiException(int status, String message) {
        super(message);
        this.status = status;
    }
}
