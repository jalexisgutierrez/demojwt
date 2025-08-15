package com.company.auth.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String m){
        super(m);
    }
}