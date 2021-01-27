package com.buffalo.ewaha.commons;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class PayloadTooLargeException extends Exception {

    public PayloadTooLargeException(String message){
        super(message);
    }
}
