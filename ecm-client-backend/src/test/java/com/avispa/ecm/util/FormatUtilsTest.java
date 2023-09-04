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

package com.avispa.ecm.util;

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
        assertEquals("5\u00A0500,00", FormatUtils.formatMoney(new BigDecimal("5500.00")));
    }

    @Test
    void stringToDecimalMoney() {
        assertEquals(new BigDecimal("5500.00"), FormatUtils.parseMoney("5\u00A0500,00"));
    }

    @Test
    void decimalToStringQuantity() {
        assertEquals("12,123", FormatUtils.formatQuantity(new BigDecimal("12.123")));
    }

    @Test
    void stringToDecimalQuantity() {
        assertEquals(new BigDecimal("12.123"), FormatUtils.parseQuantity("12,123"));
    }

    @Test
    void decimalToStringWholeQuantity() {
        assertEquals("12", FormatUtils.formatQuantity(new BigDecimal("12.00")));
    }

    @Test
    void stringToDecimalWholeQuantity() {
        assertEquals(new BigDecimal("12.00"), FormatUtils.parseQuantity("12,00"));
    }

    @Test
    void decimalToStringDiscount() {
        // non-breaking space used
        assertEquals("2,12", FormatUtils.formatPercent(new BigDecimal("2.12")));
    }

    @Test
    void stringToDecimalDiscount() {
        assertEquals(new BigDecimal("2.12"), FormatUtils.parsePercent("2,12"));
    }
}