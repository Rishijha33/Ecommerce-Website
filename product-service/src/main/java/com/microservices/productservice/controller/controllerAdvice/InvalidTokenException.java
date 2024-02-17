package com.microservices.productservice.controller.controllerAdvice;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message){
        super(message);
    }
}
