package com.example.springchallenge.controllers;

import com.example.springchallenge.dtos.ErrorDTO;
import com.example.springchallenge.exceptions.InvalidFilterException;
import com.example.springchallenge.exceptions.NoMatchesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerController {

    private final Integer BAD_REQUEST_STATUS = 400;
//    private final Integer OK_STATUS = 200;
    private final Integer INTERNAL_SERVER_ERROR_STATUS = 500;

    @ExceptionHandler(value={InvalidFilterException.class})
    public ResponseEntity<ErrorDTO> InvalidFilterExceptionHandler(InvalidFilterException e){
        ErrorDTO errorDTO = new ErrorDTO("Invalid filter", e.getMessage(), BAD_REQUEST_STATUS);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value={NoMatchesException.class})
//    public ResponseEntity<ErrorDTO> NoMatchesExceptionHandler(NoMatchesException e){
//        ErrorDTO errorDTO = new ErrorDTO("No matches", e.getMessage(), OK_STATUS);
//        return new ResponseEntity<>(errorDTO, HttpStatus.OK);
//    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<ErrorDTO> ServerErrorExceptionHandler(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("There was a problem processing your request", e.getMessage(), INTERNAL_SERVER_ERROR_STATUS);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
