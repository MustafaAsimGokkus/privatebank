package com.bank.privatebnk.exception;

public class BalanceNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BalanceNotAvailableException(String message) {

        super(message);
    }
}
