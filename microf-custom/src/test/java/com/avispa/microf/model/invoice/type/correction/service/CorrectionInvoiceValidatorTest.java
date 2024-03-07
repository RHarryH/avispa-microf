/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice.type.correction.service;

import com.avispa.microf.model.invoice.position.PositionMapper;
import com.avispa.microf.util.InvoiceUtils;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Rafał Hiszpański
 */
class CorrectionInvoiceValidatorTest {
    private final PositionMapper positionMapper = Mappers.getMapper(PositionMapper.class);
    private final CorrectionInvoiceValidator validator = new CorrectionInvoiceValidator(positionMapper);

    @Test
    void givenCorrectionWithModificationOnPositions_whenValidate_thenNothingHappens() {
        var correctionInvoice = InvoiceUtils.getCorrectionInvoice();

        assertDoesNotThrow(() -> validator.validate(correctionInvoice));
    }

    @Test
    void givenCorrectionWithoutModificationOnPositions_whenValidate_thenThrowValidationException() {
        var correctionInvoice = InvoiceUtils.getCorrectionInvoice(
                Collections.singletonList(InvoiceUtils.getPosition1()),
                Collections.singletonList(InvoiceUtils.getPosition1())
        );

        assertThrows(ValidationException.class, () -> validator.validate(correctionInvoice));
    }
}