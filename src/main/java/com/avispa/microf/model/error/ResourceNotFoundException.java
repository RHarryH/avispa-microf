package com.avispa.microf.model.error;

import com.avispa.microf.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Resource not found");
    }

    public ResourceNotFoundException(Class<?> clazz) {
        super(HttpStatus.NOT_FOUND, clazz.getSimpleName() + " not found");
    }

}