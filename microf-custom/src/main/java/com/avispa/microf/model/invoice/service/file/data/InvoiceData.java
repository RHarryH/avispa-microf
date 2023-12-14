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

package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.Invoice;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class InvoiceData {
    private final String invoiceName;
    private final String seller;
    private final String buyer;

    private final LocalDate issueDate;
    private final LocalDate serviceDate;

    private final List<PositionData> positions;
    private final List<VatRowData> vatMatrix;

    private final PaymentData payment;

    private final String comments;

    public InvoiceData(Invoice invoice, Dictionary unitDict, Dictionary vatRateDict, Dictionary paymentMethodDict) {
        if(unitDict.isEmpty()) {
            throw new IllegalArgumentException("Dictionary for units cannot be empty");
        }

        if(vatRateDict.isEmpty()) {
            throw new IllegalArgumentException("Dictionary for vat rates cannot be empty");
        }

        if(paymentMethodDict.isEmpty()) {
            throw new IllegalArgumentException("Dictionary for payment methods cannot be empty");
        }

        this.invoiceName = invoice.getObjectName();

        this.seller = invoice.getSeller().format();
        this.buyer = invoice.getBuyer().format();

        this.issueDate = invoice.getIssueDate();
        this.serviceDate = invoice.getServiceDate();

        this.positions = invoice.getPositions().stream().map(position -> new PositionData(position, unitDict, vatRateDict)).toList();

        this.vatMatrix = generateVatMatrix();

        this.payment = PaymentData.of(invoice, getVatSum().getGrossValue(), paymentMethodDict);

        this.comments = invoice.getComments();
    }

    private List<VatRowData> generateVatMatrix() {
        Map<String, VatRowData> vatMatrixMap = new TreeMap<>();
        for(PositionData position : this.positions) {
            String vatRateLabel = position.getVatRateLabel();
            VatRowData vatRowData = getVatRow(vatMatrixMap, vatRateLabel);

            vatRowData.setVatRate(vatRateLabel);
            vatRowData.setNetValue(vatRowData.getNetValue().add(position.getNetValue()));
            vatRowData.setVat(vatRowData.getVat().add(position.getVat()));
            vatRowData.setGrossValue(vatRowData.getGrossValue().add(position.getGrossValue()));
        }

        VatRowData vatSum = getVatSum(vatMatrixMap);

        return Stream.concat(
                vatMatrixMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue),
                        Stream.of(vatSum))
                .toList();
    }

    private VatRowData getVatRow(Map<String, VatRowData> vatMatrix, String vatRateLabel) {
        VatRowData vatRowData;

        if(vatMatrix.containsKey(vatRateLabel)) {
            vatRowData = vatMatrix.get(vatRateLabel);
        } else {
            vatRowData = new VatRowData();
            vatMatrix.put(vatRateLabel, vatRowData);
        }
        return vatRowData;
    }

    private VatRowData getVatSum(Map<String, VatRowData> vatMatrixMap) {
        VatRowData vatSum = new VatRowData();

        for(VatRowData vatRowData : vatMatrixMap.values()) {
            vatSum.accumulate(
                    vatRowData.getNetValue(),
                    vatRowData.getVat(),
                    vatRowData.getGrossValue());
        }

        return vatSum;
    }

    public VatRowData getVatSum() {
        return vatMatrix.get(vatMatrix.size() - 1);
    }
}
