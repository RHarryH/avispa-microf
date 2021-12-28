package com.avispa.microf.model.invoice.position;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.constants.Unit;
import com.avispa.microf.constants.VatRate;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
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
    @Column(precision=8, scale=3)
    @Digits(integer=5, fraction=3)
    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal amount;

    private Unit unit;

    @Column(precision=9, scale=2)
    @Digits(integer=7, fraction=2)
    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal unitPrice;

    @Column(precision=9, scale=2)
    @Digits(integer=7, fraction=2)
    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
    private BigDecimal discount;

    private VatRate vatRate;

    public void setPositionName(String positionName) {
        this.setObjectName(positionName);
    }

    public String getPositionName() {
        return getObjectName();
    }
}
