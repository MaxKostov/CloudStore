package com.store.authservice.exceptions;

public class SessionDoesntExistException extends RuntimeException {
    public SessionDoesntExistException() {
        super("Session not found");
    }
}
