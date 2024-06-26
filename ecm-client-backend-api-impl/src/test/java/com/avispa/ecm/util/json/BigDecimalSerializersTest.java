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

package com.avispa.ecm.util.json;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Locale;

import static com.avispa.ecm.util.json.SerializationTestUtils.serialize;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rafał Hiszpański
 */
class BigDecimalSerializersTest {

    @BeforeAll
    public static void init() {
        Locale.setDefault(new Locale("pl", "PL"));
    }

    @ParameterizedTest
    @CsvSource(value = {"1.10,'1,10'", "1.2,'1,20'", "1.567,'1,57'", "1500,'1\u00A0500,00'"})
    void serializeMoney(BigDecimal input, String expected) {
        String serialized = serialize(input, new MoneySerializer());
        assertThat(serialized).hasToString("\"" + expected + "\"");
    }

    @ParameterizedTest
    @CsvSource(value = {"200,'200'", "2000.01,'2000'", "1.567,'2'"})
    void serializePercent(BigDecimal input, String expected) {
        String serialized = serialize(input, new PercentSerializer());
        assertThat(serialized).hasToString("\"" + expected + "\"");
    }

    @ParameterizedTest
    @CsvSource(value = {"200,'200'", "2000.01,'2000,01'", "2.00,'2'", "1.567,'1,567'", "1.5678,'1,568'"})
    void serializeQuantity(BigDecimal input, String expected) {
        String serialized = serialize(input, new QuantitySerializer());
        assertThat(serialized).hasToString("\"" + expected + "\"");
    }
}