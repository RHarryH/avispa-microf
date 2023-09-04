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
import com.avispa.ecm.util.json.MoneyDeserializer;
import com.avispa.ecm.util.json.MoneySerializer;
import com.avispa.ecm.util.json.PercentDeserializer;
import com.avispa.ecm.util.json.PercentSerializer;
import com.avispa.ecm.util.json.QuantityDeserializer;
import com.avispa.ecm.util.json.QuantitySerializer;
import com.avispa.microf.model.invoice.position.Position;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class PositionData {
    private final String positionName;

    @JsonDeserialize(using = QuantityDeserializer.class)
    @JsonSerialize(using = QuantitySerializer.class)
    private final BigDecimal quantity;

    private final String unit;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private final BigDecimal unitPrice;

    @JsonDeserialize(using = PercentDeserializer.class)
    @JsonSerialize(using = PercentSerializer.class)
    private final BigDecimal discount;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal price;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal netValue;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal vat;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal grossValue;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
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
