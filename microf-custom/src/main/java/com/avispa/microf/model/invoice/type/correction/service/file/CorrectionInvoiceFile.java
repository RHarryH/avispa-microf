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

package com.avispa.microf.model.invoice.type.correction.service.file;

import com.avispa.ecm.util.FormatUtils;
import com.avispa.ecm.util.SpringContext;
import com.avispa.ecm.util.Version;
import com.avispa.microf.model.invoice.service.file.BaseInvoiceFile;
import com.avispa.microf.model.invoice.service.file.data.PaymentData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatMatrix;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.model.invoice.service.file.variable.Variable;
import com.avispa.microf.model.invoice.type.correction.service.file.data.CorrectionInvoiceData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class CorrectionInvoiceFile extends BaseInvoiceFile<CorrectionInvoiceData> {
    public CorrectionInvoiceFile(String templatePath) {
        super(templatePath);
    }

    @Override
    protected Map<String, String> fillVariables(CorrectionInvoiceData invoiceData, String issuerName) {
        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", invoiceData.getInvoiceName());
        variables.put("seller", invoiceData.getSeller());
        variables.put("buyer", invoiceData.getBuyer());
        variables.put("issue_date", FormatUtils.format(invoiceData.getIssueDate()));
        variables.put("service_date", FormatUtils.format(invoiceData.getServiceDate()));

        PaymentData paymentData = invoiceData.getPayment();
        variables.put("paid_amount", FormatUtils.formatMoney(paymentData.getPaidAmount()));
        variables.put("paid_amount_date", paymentData.getPaidAmountDate());

        variables.put("amount", FormatUtils.formatMoney(paymentData.getAmount()));
        variables.put("amount_in_words", paymentData.getAmountInWords());

        variables.put("payment_status", paymentData.getStatus());
        variables.put("payment_method", paymentData.getMethod());
        variables.put("payment_deadline", paymentData.getDeadlineDate());

        variables.put("bank_name", paymentData.getBankName());
        variables.put("bank_account_number", paymentData.getBankAccountNumber());

        variables.put("comments", invoiceData.getComments());

        variables.put("issuer_signature", issuerName);
        variables.put("version", SpringContext.getBean("microfVersion", Version.class).releaseNumber());

        variables.put("original_invoice_number", invoiceData.getOriginalInvoiceName());
        variables.put("original_issue_date", FormatUtils.format(invoiceData.getOriginalIssueDate()));
        variables.put("correction_reason", invoiceData.getCorrectionReason());

        createPositionsVariables(invoiceData.getOriginalPositions(), invoiceData.getCorrectedPositions(), invoiceData.getPositionsDiff(), variables);
        createVatMatrixVariables(invoiceData.getOriginalVatMatrix(), invoiceData.getCorrectedVatMatrix(), invoiceData.getVatMatrixDiff(), variables);

        return variables;
    }

    private void createPositionsVariables(List<PositionData> originals, List<PositionData> corrections, List<PositionData> differences, Map<String, String> variables) {
        if (originals.size() != corrections.size() || originals.size() != differences.size()) {
            throw new IllegalArgumentException("TODO: same size");
        }

        int row = 0;
        for (int i = 0; i < originals.size(); i++) {
            PositionData original = originals.get(i);
            PositionData correction = corrections.get(i);
            PositionData difference = differences.get(i);

            String rowName = String.valueOf(i + 1);
            if (original.equals(correction)) {
                createPositionVariables(variables, original, row++, rowName);
            } else {
                createPositionVariables(variables, original, row++, rowName, PositionType.BEFORE_CORRECTION);
                createPositionVariables(variables, correction, row++, rowName, PositionType.AFTER_CORRECTION);
                createPositionVariables(variables, difference, row++, rowName, PositionType.CORRECTED);
            }
        }

        variables.put(Variable.V_POSITIONS_SIZE, String.valueOf(row));
    }

    private void createVatMatrixVariables(VatMatrix originals, VatMatrix corrections, VatMatrix differences, Map<String, String> variables) {
        if (originals.vatRates().size() != corrections.vatRates().size() ||
                originals.vatRates().size() != differences.vatRates().size()) {
            throw new IllegalArgumentException("Original, correction and difference VAT matrices should have the same size!");
        }

        int row = 0;

        // original sum
        var originalVatSum = originals.summary();
        createVatRowVariables(variables, originalVatSum, row++, "Przed korektą");

        // corrected
        var correctedVatSum = corrections.summary();
        createVatRowVariables(variables, correctedVatSum, row++, "Po korekcie");

        // corrected vat rates
        List<VatRowData> fullMatrix = differences.fullMatrix();

        for (VatRowData vatRow : fullMatrix) {
            String vatRate = vatRow.getVatRate() == null ? TOTAL_TEXT : vatRow.getVatRate();
            createVatRowVariables(variables, vatRow, row++, vatRate);
        }

        variables.put(Variable.V_VAT_MATRIX_SIZE, String.valueOf(row));
    }
}
