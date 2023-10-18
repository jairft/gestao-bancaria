package com.banco.unifinance.transacoescontasclientes.exception;

public class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String message) {
        super(message);
    }

    public CustomBadRequestException() {
    }
}