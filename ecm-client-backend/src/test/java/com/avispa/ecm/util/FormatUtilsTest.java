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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rafał Hiszpański
 */
class FormatUtilsTest {
    @BeforeAll
    public static void init() {
        Locale.setDefault(new Locale("pl", "PL"));
    }

    @ParameterizedTest
    @CsvSource(value = {"1.10,'1,10'", "1.2,'1,20'", "1.567,'1,57'", "1500,'1\u00A0500,00'"})
    void formatMoney(BigDecimal input, String expected) {
        assertThat(FormatUtils.formatMoney(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"200,'200,00'", "2000.01,'2000,01'", "1.567,'1,57'"})
    void formatPercent(BigDecimal input, String expected) {
        assertThat(FormatUtils.formatPercent(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"200,'200'", "2000.01,'2000,01'", "2.00,'2'", "1.567,'1,567'", "1.5678,'1,568'"})
    void formatQuantity(BigDecimal input, String expected) {
        assertThat(FormatUtils.formatQuantity(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"'1,10',1.10",
            "'1,20',1.20",
            "'1\u00A0500,00',1500.00"})
    void parseMoney(String input, BigDecimal expected) {
        assertThat(FormatUtils.parseMoney(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"'200,00',200.00",
            "'2000,01',2000.01"})
    void parsePercent(String input, BigDecimal expected) {
        assertThat(FormatUtils.parsePercent(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"'200',200",
            "'12,00',12.00",
            "'2000,01',2000.01",
            "'1,567',1.567",
            "'1,5678',1.5678"})
    void parseQuantity(String input, BigDecimal expected) {
        assertThat(FormatUtils.parseQuantity(input)).isEqualTo(expected);
    }
}