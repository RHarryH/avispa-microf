package com.avispa.microf.model.invoice.service.file.data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class VatRowData {
    private BigDecimal netValue = BigDecimal.ZERO;
    private BigDecimal vat = BigDecimal.ZERO;
    private BigDecimal grossValue = BigDecimal.ZERO;
}
