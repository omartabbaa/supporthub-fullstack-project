package com.SupportHub.demo.exceptions;

public class RecordNotFoundExeption extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecordNotFoundExeption(){
        super();
    }

    public RecordNotFoundExeption(String message){
        super(message);
    }
}
