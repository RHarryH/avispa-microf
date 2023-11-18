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

package com.avispa.ecm.util.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ValidationErrorUtilTest {
    @Mock
    private BindingResult bindingResult;

    @Test
    void givenNoErrors_whenValidate_thenDoNothing() {
        when(bindingResult.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> ValidationErrorUtil.processErrors(bindingResult));
    }

    @Test
    void givenErrors_whenValidate_thenThrowException() {
        FieldError fieldError = new FieldError("Document", "objectName", "Field error");

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        assertThrows(ResponseStatusException.class, () -> ValidationErrorUtil.processErrors(bindingResult));
    }
}