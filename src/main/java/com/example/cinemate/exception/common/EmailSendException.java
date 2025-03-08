package com.example.cinemate.exception.common;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message) {
        super(message);
    }
}
