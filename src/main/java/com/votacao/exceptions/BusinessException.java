package com.votacao.exceptions;

public class BusinessException extends RuntimeException  {

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }
}
