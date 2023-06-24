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
    private String vatRate;
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
