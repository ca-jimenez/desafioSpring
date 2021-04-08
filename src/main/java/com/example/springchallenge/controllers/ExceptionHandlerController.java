package com.example.springchallenge.controllers;

import com.example.springchallenge.dtos.ErrorDTO;
import com.example.springchallenge.exceptions.InsufficientStockException;
import com.example.springchallenge.exceptions.InvalidArticleException;
import com.example.springchallenge.exceptions.InvalidFilterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerController {

    private final Integer BAD_REQUEST_STATUS = 400;
    private final Integer UNPROCESSABLE_ENTITY_STATUS = 422;
    private final Integer INTERNAL_SERVER_ERROR_STATUS = 500;

    @ExceptionHandler(value = {InvalidFilterException.class})
    public ResponseEntity<ErrorDTO> InvalidFilterExceptionHandler(InvalidFilterException e) {
        ErrorDTO errorDTO = new ErrorDTO("Invalid filter", e.getMessage(), BAD_REQUEST_STATUS);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidArticleException.class})
    public ResponseEntity<ErrorDTO> InvalidArticleExceptionHandler(InvalidArticleException e) {
        ErrorDTO errorDTO = new ErrorDTO("Invalid Article", e.getMessage(), BAD_REQUEST_STATUS);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InsufficientStockException.class})
    public ResponseEntity<ErrorDTO> InsufficientStockExceptionHandler(InsufficientStockException e) {
        ErrorDTO errorDTO = new ErrorDTO("Insufficient Stock", e.getMessage(), UNPROCESSABLE_ENTITY_STATUS);
        return new ResponseEntity<>(errorDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorDTO> ServerErrorExceptionHandler(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO("There was a problem processing your request", e.getMessage(), INTERNAL_SERVER_ERROR_STATUS);
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
