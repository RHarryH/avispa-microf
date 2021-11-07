package com.avispa.microf.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Rafał Hiszpański
 */
public class FormatUtils {
    public static final MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DECIMAL_FORMAT = "#,##0.00";
    private static final DecimalFormat decimalFormat;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        decimalFormat = new DecimalFormat(DEFAULT_DECIMAL_FORMAT, symbols);
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
