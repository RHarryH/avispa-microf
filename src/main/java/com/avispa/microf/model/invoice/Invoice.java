package com.avispa.microf.model.invoice;

import com.avispa.microf.constants.VatTaxRate;
import com.avispa.microf.model.converter.BigDecimalConverter;
import com.avispa.microf.numeral.NumeralToStringConverter;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Invoice implements Serializable {
    private static final String INVOICE_NUMBER_TEMPLATE = "F/%d/%s/%s";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "serial_number")
    private int serialNumber;

    @Column(name = "invoice_date", columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDate;

    @Column(name = "service_date", columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate serviceDate;

    @Convert(converter = BigDecimalConverter.class)
    @Column(name = "value")
    private BigDecimal value;

    private String invoiceNumber;
    private LocalDate paymentDate;
    private BigDecimal vat;
    private BigDecimal grossValue;
    private String grossValueInWords;

    // TODO: private List<Position> positions;
    // TODO: list of vat taxes summarized

    public Invoice() {
        super();
    }

    public void computeIndirectValues() {
        this.paymentDate = this.invoiceDate.plusDays(14);
        this.invoiceNumber = getInvoiceNumber(this.invoiceDate.getYear(), this.invoiceDate.getMonthValue(), this.serialNumber);

        this.vat = VatTaxRate.VAT_23.multiply(this.value);
        this.grossValue = this.value.add(this.vat);
        this.grossValueInWords = NumeralToStringConverter.convert(this.grossValue);
    }

    private String getInvoiceNumber(int year, int month, int serialNumber) {
        String monthPadded = StringUtils.leftPad(Integer.toString(month), 2, "0");
        String serialPadded = StringUtils.leftPad(Integer.toString(serialNumber), 3, "0");

        return String.format(INVOICE_NUMBER_TEMPLATE, year, monthPadded, serialPadded);
    }

    public String getInvoiceDateAsString() {
        return FormatUtils.format(invoiceDate);
    }

    public String getServiceDateAsString() {
        return FormatUtils.format(serviceDate);
    }

    public String getPaymentDateAsString() {
        return FormatUtils.format(paymentDate);
    }

    public String getValueAsString() {
        return FormatUtils.format(value);
    }

    public String getVatAsString() {
        return FormatUtils.format(vat);
    }

    public String getGrossValueAsString() {
        return FormatUtils.format(grossValue);
    }
}
