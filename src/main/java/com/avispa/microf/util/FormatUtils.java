package com.avispa.microf.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Rafał Hiszpański
 */
public class FormatUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static DecimalFormat decimalFormat;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("0.00", symbols);
    }

    private FormatUtils() {

    }

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String format(BigDecimal value) {
        return decimalFormat.format(value);
    }

    public static DecimalFormat getDecimalFormatter() {
        return decimalFormat;
    }

    public static char getDecimalSeparator() { return decimalFormat.getDecimalFormatSymbols().getDecimalSeparator(); }
}
