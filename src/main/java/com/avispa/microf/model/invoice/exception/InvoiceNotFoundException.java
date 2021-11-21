package com.avispa.microf.model.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "invoice not found")
public class InvoiceNotFoundException extends RuntimeException {
}