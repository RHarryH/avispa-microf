package com.avispa.microf.model.invoice.position;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Position on the invoice. Position name is object name.
 *
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Position extends EcmObject {
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Column(precision=8, scale=3)
    private BigDecimal quantity;

    private String unit;

    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Column(precision=9, scale=2)
    private BigDecimal unitPrice;

    @NumberFormat(style = NumberFormat.Style.PERCENT)
    @Column(precision=5, scale=2)
    private BigDecimal discount;

    private String vatRate;

    public void setPositionName(String positionName) {
        this.setObjectName(positionName);
    }

    public String getPositionName() {
        return getObjectName();
    }
}
