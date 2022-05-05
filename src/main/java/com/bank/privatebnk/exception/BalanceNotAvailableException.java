package com.bank.privatebnk.exception;

public class BalanceNotAvailableException extends RuntimeException {

    public BalanceNotAvailableException(String message) {

        super(message);
    }
}
