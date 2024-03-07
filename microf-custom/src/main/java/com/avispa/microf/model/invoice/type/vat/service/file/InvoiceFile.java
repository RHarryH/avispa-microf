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

package com.avispa.microf.model.invoice.type.vat.service.file;

import com.avispa.ecm.util.FormatUtils;
import com.avispa.ecm.util.SpringContext;
import com.avispa.ecm.util.Version;
import com.avispa.microf.model.invoice.service.file.BaseInvoiceFile;
import com.avispa.microf.model.invoice.service.file.data.PaymentData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatMatrix;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.model.invoice.service.file.variable.Variable;
import com.avispa.microf.model.invoice.type.vat.service.file.data.InvoiceData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class InvoiceFile extends BaseInvoiceFile<InvoiceData> {

    public InvoiceFile(String templatePath) {
        super(templatePath);
    }

    @Override
    protected Map<String, String> fillVariables(InvoiceData invoiceData, String issuerName) {
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

        createPositionsVariables(invoiceData.getPositions(), variables);
        createVatMatrixVariables(invoiceData.getVatMatrix(), variables);

        return variables;
    }

    private void createPositionsVariables(List<PositionData> positions, Map<String, String> variables) {
        variables.put(Variable.V_POSITIONS_SIZE, Integer.toString(positions.size()));
        for(int row = 0; row < positions.size(); row++) {
            PositionData position = positions.get(row);

            createPositionVariables(variables, position, row, String.valueOf(row + 1));
        }
    }

    private void createVatMatrixVariables(VatMatrix vatMatrix, Map<String, String> variables) {
        List<VatRowData> fullMatrix = vatMatrix.fullMatrix();

        variables.put(Variable.V_VAT_MATRIX_SIZE, Integer.toString(fullMatrix.size()));

        for (int row = 0; row < fullMatrix.size(); row++) {
            VatRowData vatRow = fullMatrix.get(row);

            String vatRate = vatRow.getVatRate() == null ? TOTAL_TEXT : vatRow.getVatRate();
            createVatRowVariables(variables, vatRow, row, vatRate);
        }
    }
}
