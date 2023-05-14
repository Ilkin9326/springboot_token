package com.bhos.ticketbackend.exception;

public class RoleUniqueException extends RuntimeException{
    public RoleUniqueException(String message) {
        super(message);
    }

    public RoleUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
}
