package com.avispa.microf.model.error;

import com.avispa.microf.util.api.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public class ErrorUtil {

    private ErrorUtil() {

    }

    public static void processErrors(HttpStatus status, BindingResult result) {
        result.getFieldErrors()
                .forEach(f -> log.error("{}: {}", f.getField(), f.getDefaultMessage()));

        FieldError fe = result.getFieldError();
        throw new ApiException(status, null != fe ? fe.getDefaultMessage() : "Unknown message error");
    }
}
