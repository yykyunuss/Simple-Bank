package com.eteration.simplebanking.exception;

public class BankAccounNotFoundException extends Exception{
    public BankAccounNotFoundException(String message) {
        super(message);
    }

    public BankAccounNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
