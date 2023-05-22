package com.avispa.microf.model.bankaccount.exception;

import com.avispa.ecm.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

public class BankAccountInUseException extends ApiException {
    public BankAccountInUseException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}