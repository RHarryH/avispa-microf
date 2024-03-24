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

package com.avispa.ecm.util.json;

import com.avispa.ecm.testdocument.simple.TestDocument;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rafał Hiszpański
 */
class BigDecimalDeserializersTest {

    private ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        Locale.setDefault(new Locale("pl", "PL"));
    }

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @ParameterizedTest
    @CsvSource(value = {"'1,10',1.10",
            "'1,20',1.20",
            "'1\u00A0500,00',1500.00"})
    void deserializeMoney(String input, BigDecimal expected) {
        deserialize(input, expected, new MoneyDeserializer());
    }

    @ParameterizedTest
    @CsvSource(value = {"'200,00',200.00",
            "'2000,01',2000.01"})
    void deserializePercent(String input, BigDecimal expected) {
        deserialize(input, expected, new PercentDeserializer());
    }

    @ParameterizedTest
    @CsvSource(value = {"'200',200",
            "'12,00',12.00",
            "'2000,01',2000.01",
            "'1,567',1.567",
            "'1,5678',1.5678"})
    void deserializeQuantity(String input, BigDecimal expected) {
        deserialize(input, expected, new QuantityDeserializer());
    }

    @SneakyThrows
    private void deserialize(String input, BigDecimal expected, JsonDeserializer<BigDecimal> deserializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BigDecimal.class, deserializer);
        objectMapper.registerModule(module);

        TestDocument testDocument = objectMapper.readValue("{\"unitPrice\": \"" + input + "\"}", TestDocument.class);

        Assertions.assertThat(testDocument.getUnitPrice()).isEqualTo(expected);
    }
}