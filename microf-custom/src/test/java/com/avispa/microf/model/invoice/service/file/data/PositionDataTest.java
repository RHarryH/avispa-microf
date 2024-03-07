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

package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.position.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class PositionDataTest {
    @Mock
    private Dictionary unit;
    @Mock
    private Dictionary vatRate;

    @Test
    void equalityTest() {
        when(unit.getLabel("HOUR")).thenReturn("godz.");
        when(vatRate.getColumnValue(eq("VAT_08"), any())).thenReturn("8");
        when(vatRate.getLabel("VAT_08")).thenReturn("8%");

        Position position1 = getFirstPosition();
        PositionData data1 = new PositionData(position1, unit, vatRate);

        Position position2 = getSecondPosition();
        PositionData data2 = new PositionData(position2, unit, vatRate);

        assertEquals(data1, data2);
    }

    private static Position getFirstPosition() {
        Position position1 = new Position();
        position1.setObjectName("Name");
        position1.setUnit("HOUR");
        position1.setVatRate("VAT_08");
        position1.setUnitPrice(BigDecimal.ONE);
        position1.setDiscount(BigDecimal.TEN);
        position1.setQuantity(BigDecimal.ONE);

        return position1;
    }

    private static Position getSecondPosition() {
        Position position2 = new Position();
        position2.setObjectName("Name");
        position2.setUnit("HOUR");
        position2.setVatRate("VAT_08");

        // normally these numbers would not be equal to ONE, TEN and ZERO from BigDecimal but thanks to
        // Lomboks annotation trailing zeroes are stripped
        position2.setUnitPrice(new BigDecimal("1.0000"));
        position2.setDiscount(new BigDecimal("10.000"));
        position2.setQuantity(new BigDecimal("1.00000"));

        return position2;
    }

    @Test
    void differenceCalculationTest() {
        when(unit.getLabel("HOUR")).thenReturn("godz.");
        when(unit.getLabel("PIECE")).thenReturn("szt.");
        when(vatRate.getColumnValue(eq("VAT_08"), any())).thenReturn("0.08");
        when(vatRate.getColumnValue(eq("VAT_23"), any())).thenReturn("0.23");
        when(vatRate.getLabel("VAT_08")).thenReturn("8%");
        when(vatRate.getLabel("VAT_23")).thenReturn("23%");

        PositionData data1 = getOriginalPositionData();
        PositionData data2 = getCorrectedPositionData();

        PositionData diff = new PositionData(data1, data2);

        assertAll(
                () -> assertEquals("Another Name", diff.getPositionName()),
                () -> assertEquals("szt.", diff.getUnit()),
                () -> assertEquals("23%", diff.getVatRateLabel()),

                () -> assertEquals(BigDecimal.ONE, diff.getUnitPrice()),
                () -> assertEquals(new BigDecimal("-5"), diff.getDiscount()),
                () -> assertEquals(new BigDecimal("2"), diff.getQuantity()),

                () -> assertEquals(BigDecimal.ONE, diff.getPrice().stripTrailingZeros()),
                () -> assertEquals(new BigDecimal("4.80"), diff.getNetValue()),
                () -> assertEquals(new BigDecimal("1.2390"), diff.getVat()),
                () -> assertEquals(new BigDecimal("6.0390"), diff.getGrossValue()),
                () -> assertEquals(new BigDecimal("0.23"), diff.getVatRate())
        );
    }

    private PositionData getOriginalPositionData() {
        Position position1 = new Position();
        position1.setObjectName("Name");
        position1.setUnit("HOUR");
        position1.setVatRate("VAT_08");
        position1.setUnitPrice(BigDecimal.ONE);
        position1.setDiscount(BigDecimal.TEN);
        position1.setQuantity(BigDecimal.ONE);

        return new PositionData(position1, unit, vatRate);
    }

    private PositionData getCorrectedPositionData() {
        Position position2 = new Position();
        position2.setObjectName("Another Name");
        position2.setUnit("PIECE");
        position2.setVatRate("VAT_23");
        position2.setUnitPrice(new BigDecimal("2"));
        position2.setDiscount(new BigDecimal("5"));
        position2.setQuantity(new BigDecimal("3"));

        return new PositionData(position2, unit, vatRate);
    }
}