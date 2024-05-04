package com.kodilla.currencyexchange.exception;

public class LoginAlreadyInUseException extends Throwable {

    public LoginAlreadyInUseException(String message) {
        super(message);
    }
}
