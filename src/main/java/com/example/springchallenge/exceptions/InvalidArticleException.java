package com.example.springchallenge.exceptions;

public class InvalidArticleException extends Exception {

    public InvalidArticleException(String message) {
        super(message);
    }
}