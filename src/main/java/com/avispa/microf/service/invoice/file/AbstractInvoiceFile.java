package com.avispa.microf.service.invoice.file;

import com.avispa.cms.model.content.Content;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.service.invoice.replacer.ITemplateReplacer;
import com.avispa.microf.util.SpringContext;
import com.avispa.microf.util.Version;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractInvoiceFile implements IInvoiceFile {

    private final Invoice invoice;
    protected ITemplateReplacer replacer;

    protected AbstractInvoiceFile(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public void generate() {
        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", invoice.getInvoiceNumber());
        variables.put("invoice_date", invoice.getInvoiceDateAsString());
        variables.put("service_date", invoice.getServiceDateAsString());
        variables.put("quantity", "1");
        variables.put("price_no_discount", invoice.getNetValueAsString());
        variables.put("price", invoice.getNetValueAsString());
        variables.put("net_value", invoice.getNetValueAsString());
        variables.put("vat", invoice.getVatAsString());
        variables.put("gross_value", invoice.getGrossValueAsString());
        variables.put("gross_value_in_words", invoice.getGrossValueInWords());
        variables.put("payment_date", invoice.getPaymentDateAsString());
        variables.put("comments", invoice.getComments());
        variables.put("version", SpringContext.getBean(Version.class).getReleaseNumber());

        replacer.replaceVariables(variables);
    }

    protected Content getContent(String path) {
        Content content = new Content();
        content.setExtension(getExtension());
        content.setFileStorePath(Paths.get(path, content.getUuid().toString()).toString());

        return content;
    }

    protected abstract String getExtension();

    /*protected String getInvoiceName() {
        return invoice.getInvoiceNumber().replace("/","_");
    }*/
}
