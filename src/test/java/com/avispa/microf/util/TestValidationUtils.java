package com.avispa.microf.util;

import com.avispa.microf.model.base.dto.Dto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public class TestValidationUtils {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    public static void validate(Dto dto, String expectedErrorMessage) {
        validate(dto, 1, Set.of(expectedErrorMessage));
    }

    public static void validate(Dto dto, int numberOfExpectedErrors, Set<String> expectedErrorMessages) {
        Set<ConstraintViolation<Dto>> constraintViolations =
                validator.validate(dto);
        Set<String> validationErrors = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        log.info("Found {} validation errors: {}", constraintViolations.size(), validationErrors);
        assertEquals(numberOfExpectedErrors, constraintViolations.size());
        assertEquals(expectedErrorMessages, validationErrors);
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    }
}
