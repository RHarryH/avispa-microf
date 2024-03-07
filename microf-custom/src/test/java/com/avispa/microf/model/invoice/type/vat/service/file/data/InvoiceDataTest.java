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

package com.avispa.microf.model.invoice.type.vat.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.util.InvoiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class InvoiceDataTest {
    @Mock
    private Dictionary unitDict;

    @Mock
    private Dictionary vatRateDict;

    @Mock
    private Dictionary paymentTypeDict;

    @Test
    void convertToData() {
        when(unitDict.isEmpty()).thenReturn(false);
        when(unitDict.getLabel(anyString())).thenReturn("godz.");

        when(vatRateDict.getLabel(anyString())).thenReturn("5%");
        when(vatRateDict.getColumnValue(anyString(), anyString())).thenReturn("0.05");

        when(paymentTypeDict.isEmpty()).thenReturn(false);
        when(paymentTypeDict.getLabel(anyString())).thenReturn("przelew bankowy");

        InvoiceData invoiceData = new InvoiceData(InvoiceUtils.getInvoice(), unitDict, vatRateDict, paymentTypeDict);

        PositionData positionData = invoiceData.getPositions().get(0);

        assertAll(
                () -> assertEquals("123", invoiceData.getInvoiceName()),
                () -> assertEquals("2022-07-31", invoiceData.getPayment().getDeadlineDate())
        );

        assertAll(
                () -> assertEquals("Position", positionData.getPositionName()),
                () -> assertEquals("godz.", positionData.getUnit()),
                () -> assertEquals("5%", positionData.getVatRateLabel()),

                () -> assertThat(positionData.getUnitPrice()).isEqualByComparingTo(BigDecimal.TEN),
                () -> assertThat(positionData.getDiscount()).isEqualByComparingTo(BigDecimal.ZERO),
                () -> assertThat(positionData.getQuantity()).isEqualByComparingTo(BigDecimal.ONE),

                () -> assertThat(positionData.getPrice()).isEqualByComparingTo("10.00"),
                () -> assertThat(positionData.getNetValue()).isEqualByComparingTo("10.00"),
                () -> assertThat(positionData.getVat()).isEqualByComparingTo("0.5"),
                () -> assertThat(positionData.getGrossValue()).isEqualByComparingTo("10.5"),
                () -> assertThat(positionData.getVatRate()).isEqualByComparingTo("0.05")
        );

    }
}