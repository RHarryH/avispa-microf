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
