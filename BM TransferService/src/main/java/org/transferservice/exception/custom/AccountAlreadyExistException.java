package org.transferservice.exception.custom;

public class AccountAlreadyExistException extends Exception {
    public AccountAlreadyExistException(String message) {
        super(message);
    }

}
