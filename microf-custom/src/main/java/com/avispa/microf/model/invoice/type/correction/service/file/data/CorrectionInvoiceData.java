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

package com.avispa.microf.model.invoice.type.correction.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.invoice.service.file.data.BaseInvoiceData;
import com.avispa.microf.model.invoice.service.file.data.PaymentData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatMatrix;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import lombok.Getter;
import org.springframework.data.util.StreamUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class CorrectionInvoiceData extends BaseInvoiceData {
    private final String invoiceName;
    private final String seller;
    private final String buyer;

    private final LocalDate issueDate;
    private final LocalDate serviceDate;

    private final List<PositionData> originalPositions;
    private final VatMatrix originalVatMatrix;

    private final List<PositionData> correctedPositions;
    private final VatMatrix correctedVatMatrix;

    private final List<PositionData> positionsDiff;
    private final VatMatrix vatMatrixDiff;

    private final PaymentData payment;

    private final String comments;

    private final String originalInvoiceName;
    private final LocalDate originalIssueDate;
    private final String correctionReason;

    public CorrectionInvoiceData(CorrectionInvoice invoice, Dictionary unitDict, Dictionary vatRateDict, Dictionary paymentMethodDict) {
        super(unitDict, vatRateDict, paymentMethodDict);

        Invoice originalInvoice = invoice.getOriginalInvoice();

        this.invoiceName = invoice.getObjectName();

        this.seller = originalInvoice.getSeller().format();
        this.buyer = originalInvoice.getBuyer().format();

        this.issueDate = invoice.getIssueDate();
        this.serviceDate = originalInvoice.getServiceDate();

        this.originalPositions = originalInvoice.getPositions().stream()
                .map(position -> new PositionData(position, unitDict, vatRateDict))
                .toList();
        this.correctedPositions = invoice.getPositions().stream()
                .map(position -> new PositionData(position, unitDict, vatRateDict))
                .toList();
        this.positionsDiff = getPositionDifferences();

        this.originalVatMatrix = generateVatMatrix(this.originalPositions);
        this.correctedVatMatrix = generateVatMatrix(this.correctedPositions);
        this.vatMatrixDiff = getVatRowDifferences(this.originalVatMatrix, this.correctedVatMatrix);

        this.payment = PaymentData.of(invoice.getPayment(), originalInvoice.getIssueDate(), this.correctedVatMatrix.summary().getGrossValue(), paymentMethodDict);

        this.comments = invoice.getComments();

        this.originalInvoiceName = originalInvoice.getObjectName();
        this.originalIssueDate = originalInvoice.getIssueDate();
        this.correctionReason = invoice.getCorrectionReason();
    }

    private List<PositionData> getPositionDifferences() {
        return
                StreamUtils.zip(
                                this.originalPositions.stream(),
                                this.correctedPositions.stream(),
                                PositionData::new
                        )
                        .toList();
    }

    private VatMatrix getVatRowDifferences(VatMatrix originalVatMatrix, VatMatrix correctedVatMatrix) {
        var originalVatRates = originalVatMatrix.vatRates();
        var correctedVatRates = correctedVatMatrix.vatRates();

        var differenceVatRates = new HashMap<String, VatRowData>();

        // include all differences for rates present on the original invoice or the ones removed on the correction
        originalVatRates.forEach((vatRate, original) -> {
            var corrected = correctedVatRates.containsKey(vatRate) ? correctedVatRates.get(vatRate) : new VatRowData();
            differenceVatRates.put(vatRate, getDifference(original, corrected));
        });

        // include all differences for rates not present on the original invoice (added tax rate)
        correctedVatRates.entrySet().stream()
                .filter(entry -> !differenceVatRates.containsKey(entry.getKey()))
                .forEach(entry -> {
                    var vatRate = entry.getKey();
                    var corrected = entry.getValue();
                    var original = new VatRowData(vatRate);
                    differenceVatRates.put(vatRate, getDifference(original, corrected));
                });

        return new VatMatrix(differenceVatRates,
                getDifference(originalVatMatrix.summary(), correctedVatMatrix.summary()));
    }

    private static VatRowData getDifference(VatRowData original, VatRowData corrected) {
        VatRowData diff = new VatRowData();

        diff.setVatRate(original.getVatRate());
        diff.setNetValue(corrected.getNetValue().subtract(original.getNetValue()));
        diff.setVat(corrected.getVat().subtract(original.getVat()));
        diff.setGrossValue(corrected.getGrossValue().subtract(original.getGrossValue()));

        return diff;
    }
}
