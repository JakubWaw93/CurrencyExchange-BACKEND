package com.kodilla.currencyexchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        return new ResponseEntity<>("User with provided data does not exist: " + exception, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<Object> handleCurrencyNotFoundException(CurrencyNotFoundException exception) {
        return new ResponseEntity<>("Currency with provided data does not exist: " + exception, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<Object> handleExchangeRateNotFoundException(ExchangeRateNotFoundException exception) {
        return new ResponseEntity<>("Exchange Rate with provided data does not exist: " + exception, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StationaryOfficeNotFoundException.class)
    public ResponseEntity<Object> handleStationaryOfficeNotFoundException(StationaryOfficeNotFoundException exception) {
        return new ResponseEntity<>("Stationary Office with provided data does not exist: " + exception, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> handleTransactionNotFoundException(TransactionNotFoundException exception) {
        return new ResponseEntity<>("Transaction with provided data does not exist: " + exception, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(FailedToGenerateApiKeyException.class)
    public ResponseEntity<Object> handleFailedToGenerateApiKeyException(FailedToGenerateApiKeyException exception) {
        return new ResponseEntity<>("Failed to generate Api key: " + exception, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(ExchangeRateCalculationFailedException.class)
    public ResponseEntity<Object> handleFailedExchangeRateCalculationException(ExchangeRateCalculationFailedException exception) {
        return new ResponseEntity<>("Failed to calculate Exchange Rate: " + exception, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(EmailAddressAlreadyInUseException.class)
    public ResponseEntity<Object> handleEmailAddressAlreadyInUseException(EmailAddressAlreadyInUseException exception) {
        return new ResponseEntity<>("Email already in use: " + exception, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(LoginAlreadyInUseException.class)
    public ResponseEntity<Object> handleLoginAlreadyInUseException(LoginAlreadyInUseException exception) {
        return new ResponseEntity<>("Login already in use: " + exception, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CurrencyAlreadyExistException.class)
    public ResponseEntity<Object> handleCurrencyAlreadyExistException(CurrencyAlreadyExistException exception) {
        return new ResponseEntity<>("This currency already exists: " + "\n"+exception, HttpStatus.CONFLICT);
    }


}
