package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.document.Document;
import com.avispa.microf.model.converter.BigDecimalConverter;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Invoice extends Document {
    private static final String INVOICE_NUMBER_TEMPLATE = "F/%d/%s/%s";

    @Column(name = "serial_number")
    private Integer serialNumber;

    @Column(name = "invoice_date", columnDefinition = "DATE")
    private LocalDate invoiceDate;

    @Column(name = "service_date", columnDefinition = "DATE")
    private LocalDate serviceDate;

    @Convert(converter = BigDecimalConverter.class)
    @Column(name = "net_value")
    private BigDecimal netValue;

    @Column(name = "comments")
    private String comments;

    // TODO: private List<Position> positions;
    // TODO: list of vat taxes summarized

    public Invoice() {
        super();
    }

    public String getInvoiceDateAsString() {
        return FormatUtils.format(invoiceDate);
    }

    public String getServiceDateAsString() {
        return FormatUtils.format(serviceDate);
    }

    public String getNetValueAsString() {
        return FormatUtils.format(netValue);
    }
}
