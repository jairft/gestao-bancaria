package com.banco.unifinance.gestaoclientecontas.exception;

public class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String message) {
        super(message);
    }

    public CustomBadRequestException() {
    }

    public CustomBadRequestException(Throwable cause) {
        super(cause);
    }
}