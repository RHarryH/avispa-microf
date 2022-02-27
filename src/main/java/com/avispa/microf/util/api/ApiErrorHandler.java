package com.avispa.microf.util.api;

import com.avispa.microf.util.api.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(value = { ApiException.class })
    public ResponseEntity<Object> handleApiException(ApiException e, HttpServletRequest request) {
        List<String> stackTrace = new ArrayList<>();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            stackTrace.add(stackTraceElement.toString());
        }

        ApiError error = new ApiError(e.getHttpStatus(), e.getLocalizedMessage(), request.getRequestURI(), stackTrace);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

        return new ResponseEntity<>(error, headers, e.getHttpStatus());
    }
}