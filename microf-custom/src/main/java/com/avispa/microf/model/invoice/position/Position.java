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
import com.avispa.ecm.util.json.MoneyDeserializer;
import com.avispa.ecm.util.json.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(precision=8, scale=3)
    private BigDecimal quantity;

    private String unit;

    @JsonDeserialize(using = MoneyDeserializer.class)
    @JsonSerialize(using = MoneySerializer.class)
    @Column(precision=9, scale=2)
    private BigDecimal unitPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(precision = 3)
    private BigDecimal discount;

    private String vatRate;

    public void setPositionName(String positionName) {
        this.setObjectName(positionName);
    }

    public String getPositionName() {
        return getObjectName();
    }
}
