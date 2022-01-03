package com.avispa.microf.numeral;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class NumeralToStringConverterTest {

    @Test
    void zeroTest() {
        assertEquals("zero złotych", convert("0"));
        assertEquals("zero", convert(0));
    }

    @Test
    void unitSingularTest() {
        assertEquals("jeden złoty", convert("1"));
        assertEquals("jeden", convert(1));
    }

    @Test
    void unitPluralTest() {
        assertEquals("dwa złote", convert("2"));
        assertEquals("dwa", convert(2));
    }

    @Test
    void unitPluralGenitiveTest() {
        assertEquals("siedem złotych", convert("7"));
        assertEquals("siedem", convert(7));
    }

    @Test
    void wholeTenTest() {
        assertEquals("pięćdziesiąt złotych", convert("50"));
        assertEquals("pięćdziesiąt", convert(50));
    }

    @Test
    void tenPluralTest() {
        assertEquals("pięćdziesiąt trzy złote", convert("53"));
        assertEquals("pięćdziesiąt trzy", convert(53));
    }

    @Test
    void tenPluralGenitiveTest() {
        assertEquals("pięćdziesiąt pięć złotych", convert("55"));
        assertEquals("pięćdziesiąt pięć", convert(55));
    }

    @Test
    void teenTest() {
        assertEquals("dwanaście złotych", convert("12"));
        assertEquals("dwanaście", convert(12));
    }

    @Test
    void teenTest2() {
        assertEquals("siedemnaście złotych", convert("17"));
        assertEquals("siedemnaście", convert(17));
    }

    @Test
    void wholeHundredTest() {
        assertEquals("sto złotych", convert("100"));
        assertEquals("sto", convert(100));
    }

    @Test
    void hundredSingularTest() {
        assertEquals("sto jeden złotych", convert("101"));
        assertEquals("sto jeden", convert(101));
    }

    @Test
    void hundredPluralTest() {
        assertEquals("sto dwa złote", convert("102"));
        assertEquals("sto dwa", convert(102));
    }

    @Test
    void hundredGenitiveTest() {
        assertEquals("sto pięć złotych", convert("105"));
        assertEquals("sto pięć", convert(105));
    }

    @Test
    void hundredTeenTest() {
        assertEquals("sto dwanaście złotych", convert("112"));
        assertEquals("sto dwanaście", convert(112));
    }

    @Test
    void hundredTenTest() {
        assertEquals("sto pięćdziesiąt trzy złote", convert("153"));
        assertEquals("sto pięćdziesiąt trzy", convert(153));
    }

    @Test
    void thousandTest() {
        assertEquals("jeden tysiąc złotych", convert("1000"));
        assertEquals("jeden tysiąc", convert(1000));
    }

    @Test
    void thousandUnitTest() {
        assertEquals("dwa tysiące jeden złotych", convert("2001"));
        assertEquals("dwa tysiące jeden", convert(2001));
    }

    @Test
    void thousandUnitTest2() {
        assertEquals("cztery tysiące dwieście złotych", convert("4200"));
        assertEquals("cztery tysiące dwieście", convert(4200));
    }

    @Test
    void teenThousandTest() {
        assertEquals("szesnaście tysięcy złotych", convert("16000"));
        assertEquals("szesnaście tysięcy", convert(16000));
    }

    @Test
    void mixedThousandTest() {
        assertEquals("szesnaście tysięcy sto siedemdziesiąt pięć złotych", convert("16175"));
        assertEquals("szesnaście tysięcy sto siedemdziesiąt pięć", convert(16175));
    }

    @Test
    void mixedThousandTest2() {
        assertEquals("trzynaście tysięcy dwieście sześćdziesiąt cztery złote", convert("13264"));
        assertEquals("trzynaście tysięcy dwieście sześćdziesiąt cztery", convert(13264));
    }

    @Test
    void mixedThousandTest3() {
        assertEquals("jedenaście tysięcy złotych", convert("11000"));
        assertEquals("jedenaście tysięcy", convert(11000));
    }

    @Test
    void mixedThousandTest4() {
        assertEquals("dwadzieścia jeden tysięcy sześćset czterdzieści osiem złotych", convert("21648"));
        assertEquals("dwadzieścia jeden tysięcy sześćset czterdzieści osiem", convert(21648));
    }

    @Test
    void mixedThousandTest5() {
        assertEquals("dwadzieścia tysięcy sześćset sześćdziesiąt cztery złote", convert("20664"));
        assertEquals("dwadzieścia tysięcy sześćset sześćdziesiąt cztery", convert(20664));
    }

    @Test
    void mixedThousandTest6() {
        assertEquals("dziewiętnaście tysięcy pięćdziesiąt złotych 24/100", convert("19050.24"));
        assertEquals("dziewiętnaście tysięcy pięćdziesiąt", convert(19050));
    }

    @Test
    void mixedThousandTest7() {
        assertEquals("trzydzieści dwa tysiące sześćset trzydzieści dwa złote", convert("32632"));
        assertEquals("trzydzieści dwa tysiące sześćset trzydzieści dwa", convert(32632));
    }

    @Test
    void mixedThousandTest8() {
        assertEquals("pięćset trzydzieści dwa tysiące trzydzieści osiem złotych", convert("532038"));
        assertEquals("pięćset trzydzieści dwa tysiące trzydzieści osiem", convert(532038));
    }

    @Test
    void mixedThousandTest9() {
        assertEquals("sto jeden tysięcy złotych", convert("101000"));
        assertEquals("sto jeden tysięcy", convert(101000));
    }

    @Test
    void mixedMillionTest() {
        assertEquals("dwadzieścia pięć milionów sześćset szesnaście tysięcy sto siedemdziesiąt pięć złotych", convert("25616175"));
        assertEquals("dwadzieścia pięć milionów sześćset szesnaście tysięcy sto siedemdziesiąt pięć", convert(25616175));
    }

    private String convert(String s) {
        return NumeralToStringConverter.convert(s, '.');
    }

    private String convert(int n) {
        return NumeralToStringConverter.convert(n);
    }
}