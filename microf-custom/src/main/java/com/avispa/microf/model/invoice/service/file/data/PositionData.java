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

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.position.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
@EqualsAndHashCode
public class PositionData {
    private final String positionName;

    private final BigDecimal quantity;
    private final String unit;
    private final BigDecimal unitPrice;
    private final BigDecimal discount;

    private final BigDecimal price;
    private final BigDecimal netValue;
    private final BigDecimal vat;
    private final BigDecimal grossValue;
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

        this.price = calculatePrice();
        this.netValue = calculateNetValue();
        this.vat = calculateVat();
        this.grossValue = calculateGrossValue();
    }

    public PositionData(PositionData original, PositionData correction) {
        this.positionName = correction.getPositionName();

        this.quantity = correction.getQuantity().subtract(original.getQuantity());
        this.unit = correction.getUnit();

        this.unitPrice = correction.getUnitPrice().subtract(original.getUnitPrice());
        this.discount = correction.getDiscount().subtract(original.getDiscount());

        this.vatRate = correction.getVatRate();
        this.vatRateLabel = correction.getVatRateLabel();

        this.price = correction.calculatePrice().subtract(original.calculatePrice());
        this.netValue = correction.calculateNetValue().subtract(original.calculateNetValue());
        this.vat = correction.calculateVat().subtract(original.calculateVat());
        this.grossValue = correction.calculateGrossValue().subtract(original.calculateGrossValue());
    }

    private BigDecimal calculatePrice() {
        return this.unitPrice.subtract(getDiscountValue());
    }

    /**
     * discountValue = unitPrice * discount/100 (discount is in %)
     * @return
     */
    private BigDecimal getDiscountValue() {
        return this.unitPrice.multiply(this.discount.scaleByPowerOfTen(-2));
    }

    private BigDecimal calculateNetValue() {
        return this.price.multiply(this.quantity);
    }

    private BigDecimal calculateVat() {
        return this.netValue.multiply(this.vatRate);
    }

    private BigDecimal calculateGrossValue() {
        return this.netValue.add(this.vat);
    }

    @EqualsAndHashCode.Include(replaces = "quantity")
    private BigDecimal quantityStripped() {
        return this.quantity.stripTrailingZeros();
    }

    @EqualsAndHashCode.Include(replaces = "unitPrice")
    private BigDecimal unitPriceStripped() {
        return this.unitPrice.stripTrailingZeros();
    }

    @EqualsAndHashCode.Include(replaces = "discount")
    private BigDecimal discountStripped() {
        return this.discount.stripTrailingZeros();
    }

    @EqualsAndHashCode.Include(replaces = "price")
    private BigDecimal priceStripped() {
        return this.price.stripTrailingZeros();
    }

    @EqualsAndHashCode.Include(replaces = "netValue")
    private BigDecimal netValueStripped() {
        return this.netValue.stripTrailingZeros();
    }

    @EqualsAndHashCode.Include(replaces = "vat")
    private BigDecimal vatStripped() {
        return this.vat.stripTrailingZeros();
    }

    @EqualsAndHashCode.Include(replaces = "grossValue")
    private BigDecimal grossValueStripped() {
        return this.grossValue.stripTrailingZeros();
    }
}
