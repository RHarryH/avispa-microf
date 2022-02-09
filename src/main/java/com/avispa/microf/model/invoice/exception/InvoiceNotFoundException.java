package com.avispa.microf.model.invoice.exception;

import com.avispa.microf.util.api.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvoiceNotFoundException extends ApiException {
    public InvoiceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Invoice not found");
    }
}