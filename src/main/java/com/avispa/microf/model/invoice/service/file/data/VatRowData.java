package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.microf.constants.VatRate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class VatRowData {
    private VatRate vatRate;
    private BigDecimal netValue = BigDecimal.ZERO;
    private BigDecimal vat = BigDecimal.ZERO;
    private BigDecimal grossValue = BigDecimal.ZERO;

    public void accumulate(BigDecimal netValue, BigDecimal vat, BigDecimal grossValue) {
        accumulateNetValue(netValue);
        accumulateVat(vat);
        accumulateGrossValue(grossValue);
    }

    private void accumulateNetValue(BigDecimal netValue) {
        this.netValue = this.netValue.add(netValue);
    }

    private void accumulateVat(BigDecimal vat) {
        this.vat = this.vat.add(vat);
    }

    private void accumulateGrossValue(BigDecimal grossValue) {
        this.grossValue = this.grossValue.add(grossValue);
    }
}
