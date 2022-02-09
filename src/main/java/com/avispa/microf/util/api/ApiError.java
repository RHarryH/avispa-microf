package com.avispa.microf.util.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> stackTrace;

    public ApiError(HttpStatus status, String message, List<String> stackTrace) {
        super();
        this.status = status;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        stackTrace = List.of(error);
    }
}