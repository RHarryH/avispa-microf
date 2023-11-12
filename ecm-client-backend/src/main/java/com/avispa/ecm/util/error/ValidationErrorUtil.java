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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationErrorUtil {
    public static void processErrors(BindingResult result) {
        if(result.hasErrors()) {
            result.getFieldErrors()
                    .forEach(f -> log.error("{}: {}", f.getField(), f.getDefaultMessage()));

            FieldError fe = result.getFieldError();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null != fe ? fe.getDefaultMessage() : "Unknown message error");
        }
    }
}
