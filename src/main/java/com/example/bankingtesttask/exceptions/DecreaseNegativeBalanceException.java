package com.example.bankingtesttask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DecreaseNegativeBalanceException extends Exception {
    public DecreaseNegativeBalanceException(String message){
        super(message);
    }
}
