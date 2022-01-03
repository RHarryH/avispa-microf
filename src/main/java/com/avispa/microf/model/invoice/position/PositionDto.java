package com.avispa.microf.model.invoice.position;

import com.avispa.microf.model.Dto;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

/**
 * Position on the invoice.
 *
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PositionDto implements Dto {
    private String objectName;

    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Digits(integer=5, fraction=3)
    private BigDecimal quantity = BigDecimal.ONE;

    private String unit;

    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Digits(integer=7, fraction=2)
    private BigDecimal unitPrice;

    @NumberFormat(style = NumberFormat.Style.PERCENT)
    @Digits(integer=7, fraction=2)
    private BigDecimal discount = new BigDecimal("0.00");

    private String vatRate;
}
