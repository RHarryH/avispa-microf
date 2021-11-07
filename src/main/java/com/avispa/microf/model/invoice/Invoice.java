package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.document.Document;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
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

    @Column(name = "net_value", precision=9, scale=2)
    @Digits(integer=7, fraction=2)
    @NumberFormat(pattern = FormatUtils.DEFAULT_DECIMAL_FORMAT)
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
