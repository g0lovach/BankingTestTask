package com.example.bankingtesttask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CreateClosedAccountException extends Exception{
    public CreateClosedAccountException(String message){
        super(message);
    }
}
