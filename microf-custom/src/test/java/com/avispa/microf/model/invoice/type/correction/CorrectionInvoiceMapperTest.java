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

import com.avispa.microf.model.invoice.position.PositionMapperImpl;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import com.avispa.microf.model.invoice.type.vat.InvoiceDto;
import com.avispa.microf.model.invoice.type.vat.InvoiceMapperImpl;
import com.avispa.microf.model.invoice.type.vat.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CorrectionInvoiceMapperImpl.class,
        PositionMapperImpl.class
})
class CorrectionInvoiceMapperTest {
    public static final UUID INVOICE_ID = UUID.randomUUID();
    public static final UUID ORIGINAL_INVOICE_ID = UUID.randomUUID();

    @Autowired
    private CorrectionInvoiceMapperImpl mapper;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private InvoiceMapperImpl invoiceMapper;

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        InvoiceDto invocieDto = mockAndGetInvoiceDto();

        CorrectionInvoice correctionInvoice = getSampleEntity();
        CorrectionInvoiceDto convertedDto = mapper.convertToDto(correctionInvoice);

        assertAll(() -> {
            assertEquals("FK/1", convertedDto.getObjectName());
            assertEquals("Reason", convertedDto.getCorrectionReason());
            assertNotNull(convertedDto.getOriginalInvoice());
            assertEquals(invocieDto.getObjectName(), convertedDto.getOriginalInvoice().getObjectName());
        });
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        CorrectionInvoiceDto correctionInvoiceDto = getSampleDto();
        Invoice invoice = mockAndGetInvoice();
        CorrectionInvoice convertedEntity = mapper.convertToEntity(correctionInvoiceDto);

        assertAll(() -> {
            assertEquals("FK/2", convertedEntity.getObjectName());
            assertEquals("Reason DTO", convertedEntity.getCorrectionReason());
            assertNotNull(convertedEntity.getOriginalInvoice());
            assertEquals(invoice.getObjectName(), convertedEntity.getOriginalInvoice().getObjectName());
        });
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        CorrectionInvoice correctionInvoice = getSampleEntity();
        CorrectionInvoiceDto correctionInvoiceDto = getSampleDto();

        mapper.updateEntityFromDto(correctionInvoiceDto, correctionInvoice);

        verifyNoInteractions(invoiceRepository);

        assertAll(() -> {
            assertEquals("FK/2", correctionInvoice.getObjectName());
            assertEquals("Reason DTO", correctionInvoice.getCorrectionReason());
            assertNotNull(correctionInvoice.getOriginalInvoice());
            assertEquals("F/16", correctionInvoice.getOriginalInvoice().getObjectName());
        });
    }

    @Test
    void givenDtoAndEmptyEntity_whenUpdate_thenEntityHasDtoProperties() {
        CorrectionInvoice correctionInvoice = new CorrectionInvoice();
        CorrectionInvoiceDto correctionInvoiceDto = getSampleDto();

        mapper.updateEntityFromDto(correctionInvoiceDto, correctionInvoice);

        verifyNoInteractions(invoiceRepository);

        assertAll(() -> {
            assertEquals("FK/2", correctionInvoice.getObjectName());
            assertEquals("Reason DTO", correctionInvoice.getCorrectionReason());
            assertNull(correctionInvoice.getOriginalInvoice());
        });
    }

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        CorrectionInvoice correctionInvoice = getSampleEntity();

        mapper.updateEntityFromDto(null, correctionInvoice);

        verifyNoInteractions(invoiceRepository);

        assertAll(() -> {
            assertEquals("FK/1", correctionInvoice.getObjectName());
            assertEquals("Reason", correctionInvoice.getCorrectionReason());
            assertNotNull(correctionInvoice.getOriginalInvoice());
            assertEquals("F/16", correctionInvoice.getOriginalInvoice().getObjectName());
        });
    }

    private CorrectionInvoice getSampleEntity() {
        CorrectionInvoice correctionInvoice = new CorrectionInvoice();

        correctionInvoice.setId(INVOICE_ID);
        correctionInvoice.setObjectName("FK/1");
        correctionInvoice.setCorrectionReason("Reason");

        Invoice invoice = new Invoice();
        invoice.setId(ORIGINAL_INVOICE_ID);
        invoice.setObjectName("F/16");
        invoice.setComments("Test comment");

        correctionInvoice.setOriginalInvoice(invoice);

        return correctionInvoice;
    }

    private static CorrectionInvoiceDto getSampleDto() {
        CorrectionInvoiceDto correctionInvoiceDto = new CorrectionInvoiceDto();

        correctionInvoiceDto.setId(INVOICE_ID);
        correctionInvoiceDto.setObjectName("FK/2");
        correctionInvoiceDto.setCorrectionReason("Reason DTO");

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(ORIGINAL_INVOICE_ID);
        invoiceDto.setObjectName("F/32");
        invoiceDto.setComments("Test DTO comment");

        correctionInvoiceDto.setOriginalInvoice(invoiceDto);

        return correctionInvoiceDto;
    }

    private Invoice mockAndGetInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(ORIGINAL_INVOICE_ID);
        invoice.setObjectName("F/32");
        invoice.setComments("Test comment");
        when(invoiceRepository.getReferenceById(ORIGINAL_INVOICE_ID)).thenReturn(invoice);

        return invoice;
    }

    private InvoiceDto mockAndGetInvoiceDto() {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(ORIGINAL_INVOICE_ID);
        invoiceDto.setObjectName("F/16");
        invoiceDto.setComments("Test DTO comment");
        when(invoiceMapper.convertToDto(any(Invoice.class))).thenReturn(invoiceDto);

        return invoiceDto;
    }
}