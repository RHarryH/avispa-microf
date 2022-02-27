package com.avispa.microf.util.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ApiError {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> stackTrace;
    private String path;

    public ApiError(HttpStatus status, String message, String path, String stackTrace) {
        this(status, message, path, List.of(stackTrace));
    }

    public ApiError(HttpStatus status, String message, String path, List<String> stackTrace) {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.stackTrace = stackTrace;
        this.path = path;
    }
}