package com.avispa.microf.constants;

import com.avispa.ecm.util.Displayable;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
public enum VatRate implements Displayable {
    VAT_23("23%", new BigDecimal("0.23", FormatUtils.mathContext)),
    VAT_08("8%", new BigDecimal("0.08", FormatUtils.mathContext)),
    VAT_05("5%", new BigDecimal("0.05", FormatUtils.mathContext)),
    VAT_00("0%", new BigDecimal("0.00", FormatUtils.mathContext));

    private String displayValue;
    private BigDecimal value;

    VatRate(String displayValue, BigDecimal value) {
        this.displayValue = displayValue;
        this.value = value;
    }
}
