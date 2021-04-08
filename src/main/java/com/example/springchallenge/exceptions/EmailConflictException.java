package com.example.springchallenge.exceptions;

public class EmailConflictException extends Exception {

    public EmailConflictException(String message) {
        super(message);
    }
}
