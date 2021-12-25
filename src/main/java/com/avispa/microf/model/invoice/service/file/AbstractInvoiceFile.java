package com.avispa.microf.model.invoice.service.file;

import com.avispa.microf.constants.VatTaxRate;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.customer.formatter.CorporateCustomerFormatter;
import com.avispa.microf.model.customer.formatter.CustomerFormatter;
import com.avispa.microf.model.customer.formatter.RetailCustomerFormatter;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.service.replacer.ITemplateReplacer;
import com.avispa.microf.numeral.NumeralToStringConverter;
import com.avispa.microf.util.FormatUtils;
import com.avispa.microf.util.SpringContext;
import com.avispa.microf.util.Version;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        LocalDate paymentDate = invoice.getInvoiceDate().plusDays(14);
        BigDecimal vat = VatTaxRate.VAT_23.multiply(invoice.getNetValue());
        BigDecimal grossValue = invoice.getNetValue().add(vat);
        String grossValueInWords = NumeralToStringConverter.convert(grossValue);

        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", invoice.getObjectName());
        variables.put("seller", formatCustomer(invoice.getSeller()));
        variables.put("buyer", formatCustomer(invoice.getBuyer()));
        variables.put("invoice_date", invoice.getInvoiceDateAsString());
        variables.put("service_date", invoice.getServiceDateAsString());
        variables.put("quantity", "1");
        variables.put("price_no_discount", invoice.getNetValueAsString());
        variables.put("price", invoice.getNetValueAsString());
        variables.put("net_value", invoice.getNetValueAsString());
        variables.put("vat", FormatUtils.format(vat));
        variables.put("gross_value", FormatUtils.format(grossValue));
        variables.put("gross_value_in_words", grossValueInWords);
        variables.put("payment_date", FormatUtils.format(paymentDate));
        variables.put("comments", invoice.getComments());
        variables.put("version", SpringContext.getBean(Version.class).getReleaseNumber());

        replacer.replaceVariables(variables);
    }

    private String formatCustomer(Customer customer) {
        CustomerFormatter<Customer> customerFormatter = getCustomerFormatter(customer);
        return customerFormatter.format(customer);
    }

    private CustomerFormatter getCustomerFormatter(Customer customer) {
        if(customer instanceof RetailCustomer) {
            return new RetailCustomerFormatter();
        } else if(customer instanceof CorporateCustomer) {
            return new CorporateCustomerFormatter();
        } else {
            throw new IllegalStateException("There is no known formatter for this type of customer");
        }
    }
}