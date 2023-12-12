package com.example.demo.utils;

public class UserNotLoggedException extends Exception{

    public UserNotLoggedException(String errorMessage){
        super(errorMessage);
    }
}
