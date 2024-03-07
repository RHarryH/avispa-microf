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

package com.avispa.microf.model.invoice.type.vat.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.service.file.data.BaseInvoiceData;
import com.avispa.microf.model.invoice.service.file.data.PaymentData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatMatrix;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class InvoiceData extends BaseInvoiceData {
    private final String invoiceName;
    private final String seller;
    private final String buyer;

    private final LocalDate issueDate;
    private final LocalDate serviceDate;

    private final List<PositionData> positions;
    private final VatMatrix vatMatrix;

    private final PaymentData payment;

    private final String comments;

    public InvoiceData(Invoice invoice, Dictionary unitDict, Dictionary vatRateDict, Dictionary paymentMethodDict) {
        super(unitDict, vatRateDict, paymentMethodDict);

        this.invoiceName = invoice.getObjectName();

        this.seller = invoice.getSeller().format();
        this.buyer = invoice.getBuyer().format();

        this.issueDate = invoice.getIssueDate();
        this.serviceDate = invoice.getServiceDate();

        this.positions = invoice.getPositions().stream().map(position -> new PositionData(position, unitDict, vatRateDict)).toList();

        this.vatMatrix = generateVatMatrix(this.positions);

        this.payment = PaymentData.of(invoice.getPayment(), invoice.getIssueDate(), this.vatMatrix.summary().getGrossValue(), paymentMethodDict);

        this.comments = invoice.getComments();
    }
}
