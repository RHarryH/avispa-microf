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
import com.avispa.ecm.util.json.MoneyDeserializer;
import com.avispa.ecm.util.json.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Position on the invoice.
 *
 * @author Rafał Hiszpański
 */
@Getter
@Setter
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

    private UUID id;

    @NotBlank(message = VM_POSITION_NOT_EMPTY_NOR_BLANK)
    @Size(max = 50, message = VM_POSITION_NAME_NO_LONGER)
    private String objectName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Digits(integer=5, fraction=3, message = VM_QUANTITY_OUT_OF_RANGE)
    @Positive(message = VM_QUANTITY_POSITIVE)
    private BigDecimal quantity = BigDecimal.ONE;

    @NotNull(message = VM_UNIT_NOT_NULL)
    @Dictionary(name = "Unit")
    private String unit;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    @Digits(integer=7, fraction=2, message = VM_UNIT_PRICE_OUT_OF_RANGE)
    @Positive(message = VM_UNIT_PRICE_POSITIVE)
    private BigDecimal unitPrice;

    @Digits(integer=3, fraction=2, message = VM_DISCOUNT_OUT_OF_RANGE)
    @PositiveOrZero(message = VM_DISCOUNT_POSITIVE_OR_ZERO)
    @Max(value = 100, message = VM_DISCOUNT_NO_GREATER)
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull(message = VM_VAT_RATE_NOT_NULL)
    @Dictionary(name = "VatRate")
    private String vatRate;
}
