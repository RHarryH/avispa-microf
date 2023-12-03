/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.util;

import com.avispa.ecm.model.base.dto.Dto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

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
        validate(dto, Set.of(expectedErrorMessage));
    }

    public static void validate(Dto dto, Set<String> expectedErrorMessages) {
        Set<ConstraintViolation<Dto>> constraintViolations =
                validator.validate(dto);
        Set<String> validationErrors = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        log.info("Found {} validation errors: {}", constraintViolations.size(), validationErrors);
        assertEquals(expectedErrorMessages.size(), constraintViolations.size());
        assertEquals(expectedErrorMessages, validationErrors);
    }

    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    }
}
