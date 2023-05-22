package com.avispa.ecm.model.error;

import com.avispa.ecm.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Resource not found");
    }

    public ResourceNotFoundException(Class<?> clazz) {
        super(HttpStatus.NOT_FOUND, clazz.getSimpleName() + " not found");
    }

}