package com.avispa.microf.model.customer.exception;

import com.avispa.ecm.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

public class CustomerInUseException extends ApiException {
    public CustomerInUseException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}