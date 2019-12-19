package com.avispa.microf.invoice;

import com.avispa.microf.InputParameters;
import com.avispa.microf.numeral.NumeralToStringConverter;
import com.avispa.microf.util.FormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
public class InvoiceDAO {
    private static final String INVOICE_NUMBER_TEMPLATE = "F/%d/%s/%s";

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate serviceDate;
    private LocalDate paymentDate;
    private BigDecimal value;
    private BigDecimal vat;
    private BigDecimal grossValue;
    private String grossValueInWords;

    // TODO: private List<Position> positions;
    // TODO: list of vat taxes summarized

    public InvoiceDAO(InputParameters input) {
        this.invoiceDate = input.getInvoiceDate();
        this.serviceDate = input.getServiceDate();
        this.paymentDate = invoiceDate.plusDays(14);

        setInvoiceNumber(invoiceDate.getYear(), invoiceDate.getMonthValue(), input.getSerialNumber());

        this.value = input.getValue();
        this.vat = VatTaxRate.VAT_23.multiply(value);
        this.grossValue = input.getValue().add(vat);

        this.grossValueInWords = NumeralToStringConverter.convert(grossValue);
    }

    private void setInvoiceNumber(int year, int month, int serialNumber) {
        String monthPadded = StringUtils.leftPad(Integer.toString(month), 2, "0");
        String serialPadded = StringUtils.leftPad(Integer.toString(serialNumber), 3, "0");

        this.invoiceNumber = String.format(INVOICE_NUMBER_TEMPLATE, year, monthPadded, serialPadded);
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceDate() {
        return FormatUtils.format(invoiceDate);
    }

    public String getServiceDate() {
        return FormatUtils.format(serviceDate);
    }

    public String getPaymentDate() {
        return FormatUtils.format(paymentDate);
    }

    public String getValue() {
        return FormatUtils.format(value);
    }

    public String getVat() {
        return FormatUtils.format(vat);
    }

    public String getGrossValue() {
        return FormatUtils.format(grossValue);
    }

    public String getGrossValueInWords() {
        return grossValueInWords;
    }
}
