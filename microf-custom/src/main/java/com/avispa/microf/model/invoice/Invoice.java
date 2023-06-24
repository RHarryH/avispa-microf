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

package com.avispa.microf.model.invoice;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.ecm.model.document.Document;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Invoice extends Document {
    private static final String INVOICE_NUMBER_TEMPLATE = "F/%d/%s/%s";

    @Column(name = "serial_number")
    private Integer serialNumber;

    @ManyToOne(optional = false)
    private Customer seller;

    @ManyToOne(optional = false)
    private Customer buyer;

    @Column(name = "issue_date", columnDefinition = "DATE")
    private LocalDate issueDate;

    @Column(name = "service_date", columnDefinition = "DATE")
    private LocalDate serviceDate;

    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL)
    private List<Position> positions;

    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;

    private String comments;
}
