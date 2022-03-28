package com.avispa.microf.model.invoice.position;

import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

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

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Digits(integer=5, fraction=3, message = VM_QUANTITY_OUT_OF_RANGE)
    @Positive(message = VM_QUANTITY_POSITIVE)
    private BigDecimal quantity = BigDecimal.ONE;

    @NotNull(message = VM_UNIT_NOT_NULL)
    private UUID unit;

    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Digits(integer=7, fraction=2, message = VM_UNIT_PRICE_OUT_OF_RANGE)
    @Positive(message = VM_UNIT_PRICE_POSITIVE)
    private BigDecimal unitPrice;

    @NumberFormat(style = NumberFormat.Style.PERCENT)
    @Digits(integer=3, fraction=2, message = VM_DISCOUNT_OUT_OF_RANGE)
    @PositiveOrZero(message = VM_DISCOUNT_POSITIVE_OR_ZERO)
    @Max(value = 100, message = VM_DISCOUNT_NO_GREATER)
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull(message = VM_VAT_RATE_NOT_NULL)
    private UUID vatRate;
}
