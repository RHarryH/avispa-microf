/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.document.Document;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.position.Position;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@MappedSuperclass
@Getter
@Setter
public class BaseInvoice extends Document {
    @Column(name = "serial_number")
    private Integer serialNumber;

    private LocalDate issueDate;

    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Position> positions;

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;

    private String comments;
}
