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

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
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
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String MONEY_DECIMAL_FORMAT = "#,##0.00";
    private static final String QUANTITY_DECIMAL_FORMAT = "#0.###";
    private static final String PERCENT_DECIMAL_FORMAT = "#0";

    private FormatUtils() {

    }

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static String formatMoney(BigDecimal value) {
        return format(value, MONEY_DECIMAL_FORMAT);
    }

    public static String formatPercent(BigDecimal value) {
        return format(value, PERCENT_DECIMAL_FORMAT);
    }

    public static String formatQuantity(BigDecimal value) {
        return format(value, QUANTITY_DECIMAL_FORMAT);
    }

    public static String format(BigDecimal value, String pattern) {
        return getDecimalFormatter(pattern).format(value);
    }

    public static BigDecimal parseMoney(String value) {
        // NOTE: in order to parse value to match the pattern you need to format parsed value and then
        // parse it again. For now this is not done.
        return parse(value, MONEY_DECIMAL_FORMAT);
    }

    public static BigDecimal parsePercent(String value) {
        // NOTE: in order to parse value to match the pattern you need to format parsed value and then
        // parse it again. For now this is not done.
        return parse(value, PERCENT_DECIMAL_FORMAT);
    }

    public static BigDecimal parseQuantity(String value) {
        // NOTE: in order to parse value to match the pattern you need to format parsed value and then
        // parse it again. For now this is not done.
        return parse(value, QUANTITY_DECIMAL_FORMAT);
    }

    public static BigDecimal parse(String value, String pattern) {
        try {
            return (BigDecimal) getDecimalFormatter(pattern).parse(value);
        } catch (ParseException e) {
            log.error("Can't parse {} number as BigDecimal", value);
        }

        return null;
    }

    private static DecimalFormat getDecimalFormatter(String pattern) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern); // uses default locale
        decimalFormat.setParseBigDecimal(true);

        return decimalFormat;
    }

    public static String format(BigDecimal value, String pattern, Locale locale) {
        return getDecimalFormatter(pattern, locale).format(value);
    }

    public static BigDecimal parse(String value, String pattern, Locale locale) {
        try {
            return (BigDecimal) getDecimalFormatter(pattern, locale).parse(value);
        } catch (ParseException e) {
            log.error("Can't parse {} number as BigDecimal", value);
        }

        return null;
    }

    private static DecimalFormat getDecimalFormatter(String pattern, Locale locale) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        return decimalFormat;
    }

    public static String getNewLine() {
        return System.lineSeparator();
    }
}
