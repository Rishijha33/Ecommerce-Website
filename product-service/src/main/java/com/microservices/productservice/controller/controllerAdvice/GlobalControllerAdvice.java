package com.microservices.productservice.controller.controllerAdvice;

import com.microservices.productservice.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(value = ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(Exception ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(Exception ex)
    {
        String message = ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

}
