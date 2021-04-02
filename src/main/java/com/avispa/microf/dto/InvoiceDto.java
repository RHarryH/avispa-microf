package com.avispa.microf.dto;

import com.avispa.microf.model.invoice.Invoice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements Serializable {
    private int serialNumber = 1;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate serviceDate;
    private String netValue;
    private String comments;

    public InvoiceDto() {
        super();
    }

    public Invoice convertToEntity() {
        Invoice invoice = new Invoice();

        invoice.setSerialNumber(this.serialNumber);
        invoice.setInvoiceDate(this.invoiceDate);
        invoice.setServiceDate(this.serviceDate);
        invoice.setNetValue(convertToBigDecimal(this.netValue));
        invoice.setComments(comments);

        invoice.computeIndirectValues();
        return invoice;
    }

    private BigDecimal convertToBigDecimal(String value) {
        return new BigDecimal(value.substring(value.indexOf(" ") + 1));
    }
}
