package com.avispa.microf.model.invoice.service.file;

import com.avispa.microf.model.invoice.service.file.data.InvoiceData;
import com.avispa.microf.model.invoice.service.file.data.PaymentData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.model.invoice.service.file.variable.Variable;
import com.avispa.microf.model.invoice.service.file.variable.VariableNameGenerator;
import com.avispa.microf.model.invoice.service.replacer.ITemplateReplacer;
import com.avispa.ecm.util.SpringContext;
import com.avispa.ecm.util.Version;
import com.avispa.microf.util.FormatUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractInvoiceFile implements IInvoiceFile {
    private static final String TOTAL_TEXT = "Ogółem";

    protected ITemplateReplacer replacer;

    @Override
    public void generate(InvoiceData invoiceData, String issuerName) {
        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", invoiceData.getInvoiceName());
        variables.put("seller", invoiceData.getSeller());
        variables.put("buyer", invoiceData.getBuyer());
        variables.put("issue_date", FormatUtils.format(invoiceData.getIssueDate()));
        variables.put("service_date", FormatUtils.format(invoiceData.getServiceDate()));

        createPositionsVariables(invoiceData, variables);
        createVatMatrixVariables(invoiceData, variables);

        PaymentData paymentData = invoiceData.getPayment();
        variables.put("paid_amount", FormatUtils.format(paymentData.getPaidAmount()));
        variables.put("paid_amount_date", paymentData.getPaidAmountDate());

        variables.put("amount", FormatUtils.format(paymentData.getAmount()));
        variables.put("amount_in_words", paymentData.getAmountInWords());

        variables.put("payment_status", paymentData.getStatus());
        variables.put("payment_method", paymentData.getMethod());
        variables.put("payment_deadline", paymentData.getDeadlineDate());

        variables.put("bank_name", paymentData.getBankName());
        variables.put("bank_account_number", paymentData.getBankAccountNumber());

        variables.put("comments", invoiceData.getComments());

        variables.put("issuer_signature", issuerName);
        variables.put("version", SpringContext.getBean(Version.class).getReleaseNumber());

        replacer.replaceVariables(variables);
    }

    private void createPositionsVariables(InvoiceData invoiceData, Map<String, String> variables) {
        List<PositionData> positions = invoiceData.getPositions();
        variables.put(Variable.V_POSITIONS_SIZE, Integer.toString(positions.size()));
        for(int row = 0; row < positions.size(); row++) {
            PositionData position = positions.get(row);

            variables.put(VariableNameGenerator.generatePositionName(row, 0), String.valueOf(row + 1));
            variables.put(VariableNameGenerator.generatePositionName(row, 1), position.getPositionName());
            variables.put(VariableNameGenerator.generatePositionName(row, 2), FormatUtils.format(position.getQuantity()));
            variables.put(VariableNameGenerator.generatePositionName(row, 3), position.getUnit());
            variables.put(VariableNameGenerator.generatePositionName(row, 4), FormatUtils.format(position.getUnitPrice()));
            variables.put(VariableNameGenerator.generatePositionName(row, 5), FormatUtils.format(position.getDiscount()));
            variables.put(VariableNameGenerator.generatePositionName(row, 6), FormatUtils.format(position.getPrice()));
            variables.put(VariableNameGenerator.generatePositionName(row, 7), FormatUtils.format(position.getNetValue()));
            String vatRate = position.getVatRateLabel();
            variables.put(VariableNameGenerator.generatePositionName(row, 8), vatRate.substring(0, vatRate.length() - 1)); // remove percent sign
        }
    }

    private void createVatMatrixVariables(InvoiceData invoiceData, Map<String, String> variables) {
        List<VatRowData> vatMatrix = invoiceData.getVatMatrix();
        variables.put(Variable.V_VAT_MATRIX_SIZE, Integer.toString(vatMatrix.size()));
        for(int row = 0; row < vatMatrix.size(); row++) {
            VatRowData vatRow = vatMatrix.get(row);

            String vatRate = vatRow.getVatRate() == null ? TOTAL_TEXT : vatRow.getVatRate();
            variables.put(VariableNameGenerator.generateVatMatrixName(row, 0), vatRate);
            variables.put(VariableNameGenerator.generateVatMatrixName(row, 1), FormatUtils.format(vatRow.getNetValue()));
            variables.put(VariableNameGenerator.generateVatMatrixName(row, 2), FormatUtils.format(vatRow.getVat()));
            variables.put(VariableNameGenerator.generateVatMatrixName(row, 3), FormatUtils.format(vatRow.getGrossValue()));
        }
    }
}