package com.avispa.microf.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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
    void decimalToStringAmount() {
        // non-breaking space used
        assertEquals("12,123", FormatUtils.format(FormatUtils.AMOUNT_DECIMAL_FORMAT, new BigDecimal("12.123")));
    }

    @Test
    void stringToDecimalAmount() {
        assertEquals(new BigDecimal("12.123"), FormatUtils.parse(FormatUtils.AMOUNT_DECIMAL_FORMAT, "12,123"));
    }

    @Test
    void decimalToStringWholeAmount() {
        // non-breaking space used
        assertEquals("12", FormatUtils.format(FormatUtils.AMOUNT_DECIMAL_FORMAT, new BigDecimal("12.00")));
    }

    @Test
    void stringToDecimalWholeAmount() {
        assertEquals(new BigDecimal("12.00"), FormatUtils.parse(FormatUtils.AMOUNT_DECIMAL_FORMAT, "12,00"));
    }

    @Test
    void decimalToStringDiscount() {
        // non-breaking space used
        assertEquals("2,12", FormatUtils.format(FormatUtils.AMOUNT_DECIMAL_FORMAT, new BigDecimal("2.12")));
    }

    @Test
    void stringToDecimalDiscount() {
        assertEquals(new BigDecimal("2.12"), FormatUtils.parse(FormatUtils.AMOUNT_DECIMAL_FORMAT, "2,12"));
    }
}