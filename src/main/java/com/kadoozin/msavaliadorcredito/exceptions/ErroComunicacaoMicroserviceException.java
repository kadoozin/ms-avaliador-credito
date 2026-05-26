package com.kadoozin.msavaliadorcredito.exceptions;

public class ErroComunicacaoMicroserviceException extends RuntimeException {

    private final int status;

    public ErroComunicacaoMicroserviceException(String message, int status) {
        super(message);
        this.status = status;
    }

    public ErroComunicacaoMicroserviceException(String message, int status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
