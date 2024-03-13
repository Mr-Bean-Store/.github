package com.example.devBean.exception;

public class PriceNotFoundException extends RuntimeException {
    
    public PriceNotFoundException(Long id) {
        super("Could not find price " + id);
    }
}
