package com.buffalo.ewaha.commons;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends Exception {

    public UnprocessableEntityException(String message){
        super(message);
    }
}
