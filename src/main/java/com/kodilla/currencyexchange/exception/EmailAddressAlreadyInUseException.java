package com.kodilla.currencyexchange.exception;

public class EmailAddressAlreadyInUseException extends Throwable {

    public EmailAddressAlreadyInUseException(String message) {
        super(message);
    }
}
