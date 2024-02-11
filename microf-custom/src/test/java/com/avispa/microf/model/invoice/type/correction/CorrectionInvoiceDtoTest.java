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

package com.avispa.microf.model.invoice.type.correction;

import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.type.vat.InvoiceDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static com.avispa.microf.util.TestValidationUtils.getCurrentDate;
import static com.avispa.microf.util.TestValidationUtils.validate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Rafał Hiszpański
 */
class CorrectionInvoiceDtoTest {
    private CorrectionInvoiceDto correctionInvoiceDto;

    @BeforeEach
    void createDto() {
        correctionInvoiceDto = new CorrectionInvoiceDto();
        correctionInvoiceDto.setCorrectionReason("Reason");
        correctionInvoiceDto.setIssueDate(getCurrentDate());

        PositionDto positionDto = correctionInvoiceDto.getPositions().get(0); // first position is always available when created
        positionDto.setObjectName("Name");
        positionDto.setUnit("UNIT");
        positionDto.setVatRate("RATE");
        positionDto.setUnitPrice(BigDecimal.ONE);
    }

    @Test
    void givenEmptyPositions_whenValidate_thenFail() {
        correctionInvoiceDto.setPositions(Collections.emptyList());
        validate(correctionInvoiceDto, InvoiceDto.VM_POSITIONS_NOT_EMPTY);
    }

    @Test
    void givenCorrectionReasonExceedMaxLength_whenValidate_thenFail() {
        correctionInvoiceDto.setCorrectionReason(RandomStringUtils.randomAlphabetic(201));
        validate(correctionInvoiceDto, CorrectionInvoiceDto.VM_CORRECTION_REASON_NO_LONGER);
    }

    @Test
    void givenCommentsExceedMaxLength_whenValidate_thenFail() {
        correctionInvoiceDto.setComments(RandomStringUtils.randomAlphabetic(201));
        validate(correctionInvoiceDto, InvoiceDto.VM_COMMENTS_NO_LONGER);
    }

    @Test
    void givenOriginalInvoice_whenInherit_thenPropertiesUpdated() {
        // given
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setComments("Original comment");

        PositionDto positionDto = correctionInvoiceDto.getPositions().get(0); // first position is always available when created
        positionDto.setObjectName("Original name");

        invoiceDto.setPositions(List.of(positionDto));

        correctionInvoiceDto.setOriginalInvoice(invoiceDto);

        // when
        correctionInvoiceDto.inherit();

        // then
        assertAll(() -> {
            assertEquals("Oryginalny komentarz: Original comment", correctionInvoiceDto.getComments());
            assertFalse(correctionInvoiceDto.getPositions().isEmpty());
            PositionDto actualPositionDto = correctionInvoiceDto.getPositions().get(0);
            assertEquals("Original name", actualPositionDto.getObjectName());
        });
    }
}