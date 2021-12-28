package com.avispa.microf.model.invoice.service.file;

import com.avispa.microf.constants.VatRate;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.service.file.data.InvoiceData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.model.invoice.service.replacer.ITemplateReplacer;
import com.avispa.microf.util.FormatUtils;
import com.avispa.microf.util.SpringContext;
import com.avispa.microf.util.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractInvoiceFile implements IInvoiceFile {
    protected ITemplateReplacer replacer;

    @Override
    public void generate(Invoice invoice) {
        InvoiceData invoiceData = new InvoiceData(invoice);

        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", invoiceData.getInvoiceName());
        variables.put("seller", invoiceData.getSeller());
        variables.put("buyer", invoiceData.getBuyer());
        variables.put("invoice_date", FormatUtils.format(invoiceData.getInvoiceDate()));
        variables.put("service_date", FormatUtils.format(invoiceData.getServiceDate()));

        for(PositionData position : invoiceData.getPositions()) {
            variables.put("position_order", position.getPositionOrder());
            variables.put("position_name", position.getPositionName());
            variables.put("amount", FormatUtils.format(position.getAmount()));
            variables.put("unit", position.getUnit());
            variables.put("unit_price", FormatUtils.format(position.getUnitPrice()));
            variables.put("discount", FormatUtils.format(position.getDiscount()));
            variables.put("price", FormatUtils.format(position.getPrice()));
            variables.put("net_value", FormatUtils.format(position.getNetValue()));
            variables.put("vat_rate", position.getVatRate().getText());
        }

        for(Map.Entry<VatRate, VatRowData> vatEntry : invoiceData.getVatMatrix().entrySet()) {
            variables.put("matrix_vat_rate", vatEntry.getKey().getText() + "%");
            VatRowData vatRow = vatEntry.getValue();
            variables.put("matrix_net_value", FormatUtils.format(vatRow.getNetValue()));
            variables.put("matrix_vat", FormatUtils.format(vatRow.getVat()));
            variables.put("matrix_gross_value", FormatUtils.format(vatRow.getGrossValue()));
        }

        variables.put("net_value_sum", FormatUtils.format(invoiceData.getVatSum().getNetValue()));
        variables.put("vat_sum", FormatUtils.format(invoiceData.getVatSum().getVat()));
        variables.put("gross_value_sum", FormatUtils.format(invoiceData.getVatSum().getGrossValue()));

        variables.put("gross_value_in_words", invoiceData.getGrossValueInWords());
        variables.put("payment_date", FormatUtils.format(invoiceData.getPaymentDate()));
        variables.put("comments", invoiceData.getComments());
        variables.put("version", SpringContext.getBean(Version.class).getReleaseNumber());

        replacer.replaceVariables(variables);
    }
}
