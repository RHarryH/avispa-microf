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

package com.avispa.microf.model.invoice.position;

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.ecm.util.BigDecimalUtils;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Position on the invoice.
 *
 * @author Rafał Hiszpański
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PositionDto implements Dto {
    public static final String VM_POSITION_NOT_EMPTY_NOR_BLANK = "Position name cannot be empty or blank";
    public static final String VM_POSITION_NAME_NO_LONGER = "Position name cannot be longer than 50 characters";
    public static final String VM_QUANTITY_POSITIVE = "Quantity must be greater than 0";
    public static final String VM_QUANTITY_OUT_OF_RANGE = "Quantity out of range (expected <5 digits>.<3 digits>)";
    public static final String VM_UNIT_PRICE_POSITIVE = "Unit price must be greater than 0";
    public static final String VM_UNIT_PRICE_OUT_OF_RANGE = "Unit price out of range (expected <7 digits>.<2 digits>)";
    public static final String VM_UNIT_NOT_NULL = "Unit cannot be null";
    public static final String VM_DISCOUNT_OUT_OF_RANGE = "Unit price out of range (expected <3 digits>.<2 digits>)";
    public static final String VM_DISCOUNT_POSITIVE_OR_ZERO = "Discount must be greater or equal 0%";
    public static final String VM_DISCOUNT_NO_GREATER = "Discount cannot be greater than 100%";
    public static final String VM_VAT_RATE_NOT_NULL = "VAT rate cannot be null";

    @EqualsAndHashCode.Exclude
    private UUID id;

    @NotBlank(message = VM_POSITION_NOT_EMPTY_NOR_BLANK)
    @Size(max = 50, message = VM_POSITION_NAME_NO_LONGER)
    private String objectName;

    @Digits(integer=5, fraction=3, message = VM_QUANTITY_OUT_OF_RANGE)
    @Positive(message = VM_QUANTITY_POSITIVE)
    private BigDecimal quantity = BigDecimal.ONE;

    @NotNull(message = VM_UNIT_NOT_NULL)
    @Dictionary(name = "Unit")
    private String unit;

    @Digits(integer=7, fraction=2, message = VM_UNIT_PRICE_OUT_OF_RANGE)
    @Positive(message = VM_UNIT_PRICE_POSITIVE)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Digits(integer = 3, fraction = 0, message = VM_DISCOUNT_OUT_OF_RANGE)
    @PositiveOrZero(message = VM_DISCOUNT_POSITIVE_OR_ZERO)
    @Max(value = 100, message = VM_DISCOUNT_NO_GREATER)
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull(message = VM_VAT_RATE_NOT_NULL)
    @Dictionary(name = "VatRate")
    private String vatRate;

    /**
     * Copy constructor
     *
     * @param positionDto
     */
    public PositionDto(PositionDto positionDto) {
        this.objectName = positionDto.objectName;
        this.quantity = BigDecimalUtils.clone(positionDto.quantity);
        this.unit = positionDto.unit;
        this.unitPrice = BigDecimalUtils.clone(positionDto.unitPrice);
        this.discount = BigDecimalUtils.clone(positionDto.discount);
        this.vatRate = positionDto.vatRate;
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
}
