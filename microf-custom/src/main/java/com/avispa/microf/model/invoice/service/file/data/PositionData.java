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

import com.avispa.microf.model.invoice.position.Position;
import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class PositionData {
    private final String positionName;

    @NumberFormat(pattern = FormatUtils.QUANTITY_DECIMAL_FORMAT)
    private final BigDecimal quantity;

    private final String unit;

    @NumberFormat(pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    private final BigDecimal unitPrice;

    @NumberFormat(pattern = FormatUtils.PERCENT_DECIMAL_FORMAT)
    private final BigDecimal discount;

    @NumberFormat(pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    private BigDecimal price;

    @NumberFormat(pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    private BigDecimal netValue;

    @NumberFormat(pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    private BigDecimal vat;

    @NumberFormat(pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    private BigDecimal grossValue;

    @NumberFormat(pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    private final BigDecimal vatRate;

    private final String vatRateLabel;

    public PositionData(Position position, Dictionary unitDict, Dictionary vatRateDict) {
        this.positionName = position.getPositionName();

        this.quantity = position.getQuantity();
        this.unit = unitDict.getLabel(position.getUnit());

        this.unitPrice = position.getUnitPrice();
        this.discount = position.getDiscount();

        this.vatRate = new BigDecimal(vatRateDict.getColumnValue(position.getVatRate(), "rate"));
        this.vatRateLabel = vatRateDict.getLabel(position.getVatRate());

        setPrice();
        setNetValue();
        setVat();
        setGrossValue();
    }

    private void setPrice() {
        this.price = this.unitPrice.subtract(getDiscountValue());
    }

    /**
     * discountValue = unitPrice * discount/100 (discount is in %)
     * @return
     */
    private BigDecimal getDiscountValue() {
        return this.unitPrice.multiply(this.discount.scaleByPowerOfTen(-2));
    }

    private void setNetValue() {
        this.netValue = this.price.multiply(this.quantity);
    }

    private void setVat() {
        this.vat = this.netValue.multiply(this.vatRate);
    }

    private void setGrossValue() {
        this.grossValue = this.netValue.add(this.vat);
    }
}
