package com.avispa.microf.constants;

import com.avispa.microf.util.FormatUtils;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
public final class VatTaxRate {

    private VatTaxRate() {

    }

    public static final BigDecimal VAT_23 = new BigDecimal("0.23", FormatUtils.mathContext);
    public static final BigDecimal VAT_08 = new BigDecimal("0.08", FormatUtils.mathContext);
    public static final BigDecimal VAT_05 = new BigDecimal("0.05", FormatUtils.mathContext);
    public static final BigDecimal VAT_00 = new BigDecimal("0.00", FormatUtils.mathContext);
}
