package com.example.springchallenge.exceptions;

import lombok.Data;

//@Data
public class InvalidFilterException extends Exception {

//    public Integer status;

    public InvalidFilterException(String message) {
        super(message);
//        setStatus(400);
    }

}
