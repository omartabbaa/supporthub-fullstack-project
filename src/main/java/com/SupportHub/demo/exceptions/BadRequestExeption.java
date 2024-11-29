package com.SupportHub.demo.exceptions;

public class BadRequestExeption extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadRequestExeption() {
        super();
    }

    public BadRequestExeption(String message) {
        super(message);
    }
    
}
