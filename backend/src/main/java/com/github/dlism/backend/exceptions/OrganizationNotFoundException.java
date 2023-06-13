package com.github.dlism.backend.exceptions;

public class OrganizationNotFoundException extends RuntimeException{
    public OrganizationNotFoundException(String message){
        super(message);
    }
}
