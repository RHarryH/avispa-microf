package com.avispa.microf.util.validation.exception;

import com.avispa.microf.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

/**
 * @author Rafał Hiszpański
 */
public class InvalidNipException extends ApiException {
    public InvalidNipException() {
        super(HttpStatus.BAD_REQUEST, "Vat Identification Number is invalid");
    }
}
