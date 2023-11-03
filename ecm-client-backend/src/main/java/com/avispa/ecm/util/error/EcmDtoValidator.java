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

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.util.exception.EcmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EcmDtoValidator {

    private final Validator validator;

    public <D extends Dto> void validate(D dto) {
        Set<ConstraintViolation<D>> violations = validator.validate(dto);
        String errors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
        if(!errors.isEmpty()) {
            log.error("Validation errors: {}", errors);
            throw new EcmException("Validation errors: " + errors);
        }
    }
}
