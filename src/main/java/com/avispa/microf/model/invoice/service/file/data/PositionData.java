package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.microf.constants.VatRate;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class PositionData {
    private final String positionOrder;
    private final String positionName;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private final BigDecimal amount;

    private final String unit;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private final BigDecimal unitPrice;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private final BigDecimal discount;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal price;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal netValue;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal vat;

    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal grossValue;

    private final VatRate vatRate;

    public PositionData(Position position, int i) {
        this.positionOrder = String.valueOf(i + 1);
        this.positionName = position.getPositionName();

        this.amount = position.getAmount();
        this.unit = position.getUnit().getText();

        this.unitPrice = position.getUnitPrice();
        this.discount = position.getDiscount();

        this.vatRate = position.getVatRate();

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
        this.netValue = this.price.multiply(this.amount);
    }

    private void setVat() {
        this.vat = this.netValue.multiply(this.vatRate.getValue());
    }

    private void setGrossValue() {
        this.grossValue = this.netValue.add(this.vat);
    }
}
