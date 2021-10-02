package com.avispa.microf.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements Serializable {
    private Long id;
    private int serialNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate serviceDate;
    private String netValue;
    private String comments;

    public InvoiceDto() {
        super();
    }
}
