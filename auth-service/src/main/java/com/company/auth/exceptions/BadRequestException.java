package com.company.auth.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String m){
        super(m);
    }
}