package com.bhos.ticketbackend.exception;

public class UserEmailUniqueException extends RuntimeException{
    public UserEmailUniqueException(String msg) {
        super(msg);
    }

    public UserEmailUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
}
