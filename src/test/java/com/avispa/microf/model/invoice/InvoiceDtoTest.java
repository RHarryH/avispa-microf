package com.avispa.microf.model.invoice;

import com.avispa.microf.model.invoice.position.PositionDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class InvoiceDtoTest {

    @Test
    void type() {
        InvoiceDto invoiceDto = new InvoiceDto();
        assertEquals(PositionDto.class, invoiceDto.getListType("positions"));
    }
}