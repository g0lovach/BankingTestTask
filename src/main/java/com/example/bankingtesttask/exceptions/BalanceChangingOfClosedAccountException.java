package com.example.bankingtesttask.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BalanceChangingOfClosedAccountException extends Exception{

    public BalanceChangingOfClosedAccountException(String message){
        super(message);
    }

}
