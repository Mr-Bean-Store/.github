package com.example.devBean.exception;

public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(Long id) {
        super("Could not find customer " + id);
    }

    public CustomerNotFoundException(String email) {
        super("Could not find customer " + email);
    }

}
