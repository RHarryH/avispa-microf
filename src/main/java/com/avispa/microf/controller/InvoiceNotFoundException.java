package com.avispa.microf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "invoice not found")
public class InvoiceNotFoundException extends RuntimeException {
}