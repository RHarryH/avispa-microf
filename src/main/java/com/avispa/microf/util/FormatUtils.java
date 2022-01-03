package com.avispa.microf.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public class FormatUtils {
    public static final MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String MONEY_DECIMAL_FORMAT = "#,##0.00";
    public static final String QUANTITY_DECIMAL_FORMAT = "#0.###";
    public static final String PERCENT_DECIMAL_FORMAT = "#0.00";

    private FormatUtils() {

    }

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String format(BigDecimal value) {
        return format(MONEY_DECIMAL_FORMAT, value);
    }

    public static String format(String pattern, BigDecimal value) {
        return getDecimalFormatter(pattern).format(value);
    }

    public static BigDecimal parse(String pattern, String value) {
        try {
            return (BigDecimal) getDecimalFormatter(pattern).parse(value);
        } catch (ParseException e) {
            log.error("Can't parse {} number as BigDecimal", value);
        }

        return null;
    }

    private static DecimalFormat getDecimalFormatter(String pattern) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pl"));
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        return decimalFormat;
    }

    public static String transformDecimal(String input) {
        return input
                .replace(".", ",")
                //.replace(",", ".") // set default locale decimal separator
                .replace(" ", "") // remove grouping separator (space from DTO)
                .replace("\u00A0", ""); // remove grouping separator (important - non-breaking space used)
    }

    public static String getNewLine() {
        return System.getProperty("line.separator");
    }
}
