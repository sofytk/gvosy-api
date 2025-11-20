package com.sonchasapps.dto;

public class MyException extends RuntimeException {
    public MyException(String error) {
        super(error);
    }
}