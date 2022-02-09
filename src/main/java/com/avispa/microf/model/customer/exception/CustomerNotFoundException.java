package com.avispa.microf.model.customer.exception;

import com.avispa.microf.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends ApiException {
    public CustomerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Customer not found");
    }
}