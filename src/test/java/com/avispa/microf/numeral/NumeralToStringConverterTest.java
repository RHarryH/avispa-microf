package com.avispa.microf.numeral;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class NumeralToStringConverterTest {

    @Test
    void zeroTest() {
        assertEquals("zero", convert("0"));
        assertEquals("zero", convert(0));
    }

    @Test
    void unitTest() {
        assertEquals("siedem", convert("7"));
        assertEquals("siedem", convert(7));
    }

    @Test
    void tenTest() {
        assertEquals("pięćdziesiąt trzy", convert("53"));
        assertEquals("pięćdziesiąt trzy", convert(53));
    }

    @Test
    void teenTest() {
        assertEquals("dwanaście", convert("12"));
        assertEquals("dwanaście", convert(12));
    }

    @Test
    void hundredTest() {
        assertEquals("sto", convert("100"));
        assertEquals("sto", convert(100));
    }

    @Test
    void hundredTeenTest() {
        assertEquals("sto dwanaście", convert("112"));
        assertEquals("sto dwanaście", convert(112));
    }

    @Test
    void thousandTest() {
        assertEquals("jeden tysiąc", convert("1000"));
        assertEquals("jeden tysiąc", convert(1000));
    }

    @Test
    void thousandUnitTest() {
        assertEquals("dwa tysiące jeden", convert("2001"));
        assertEquals("dwa tysiące jeden", convert(2001));
    }

    @Test
    void teenThousandTest() {
        assertEquals("szesnaście tysięcy", convert("16000"));
        assertEquals("szesnaście tysięcy", convert(16000));
    }

    @Test
    void mixedThousandTest() {
        assertEquals("szesnaście tysięcy sto siedemdziesiąt pięć", convert("16175"));
        assertEquals("szesnaście tysięcy sto siedemdziesiąt pięć", convert(16175));
    }

    @Test
    void mixedMillionTest() {
        assertEquals("dwadzieścia pięć milionów sześćset szesnaście tysięcy sto siedemdziesiąt pięć", convert("25616175"));
        assertEquals("dwadzieścia pięć milionów sześćset szesnaście tysięcy sto siedemdziesiąt pięć", convert(25616175));
    }

    @NotNull
    private String convert(String s) {
        return NumeralToStringConverter.convert(s, '.');
    }

    @NotNull
    private String convert(int n) {
        return NumeralToStringConverter.convert(n);
    }
}