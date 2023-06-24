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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class FormatUtilsTest {

    @Test
    void decimalToStringMoney() {
        // non-breaking space used
        assertEquals("5\u00A0500,00", FormatUtils.format(FormatUtils.MONEY_DECIMAL_FORMAT, new BigDecimal("5500.00")));
    }

    @Test
    void stringToDecimalMoney() {
        assertEquals(new BigDecimal("5500.00"), FormatUtils.parse(FormatUtils.MONEY_DECIMAL_FORMAT, "5\u00A0500,00"));
    }

    @Test
    void decimalToStringQuantity() {
        // non-breaking space used
        assertEquals("12,123", FormatUtils.format(FormatUtils.QUANTITY_DECIMAL_FORMAT, new BigDecimal("12.123")));
    }

    @Test
    void stringToDecimalQuantity() {
        assertEquals(new BigDecimal("12.123"), FormatUtils.parse(FormatUtils.QUANTITY_DECIMAL_FORMAT, "12,123"));
    }

    @Test
    void decimalToStringWholeQuantity() {
        // non-breaking space used
        assertEquals("12", FormatUtils.format(FormatUtils.QUANTITY_DECIMAL_FORMAT, new BigDecimal("12.00")));
    }

    @Test
    void stringToDecimalWholeQuantity() {
        assertEquals(new BigDecimal("12.00"), FormatUtils.parse(FormatUtils.QUANTITY_DECIMAL_FORMAT, "12,00"));
    }

    @Test
    void decimalToStringDiscount() {
        // non-breaking space used
        assertEquals("2,12", FormatUtils.format(FormatUtils.QUANTITY_DECIMAL_FORMAT, new BigDecimal("2.12")));
    }

    @Test
    void stringToDecimalDiscount() {
        assertEquals(new BigDecimal("2.12"), FormatUtils.parse(FormatUtils.QUANTITY_DECIMAL_FORMAT, "2,12"));
    }
}