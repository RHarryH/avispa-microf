package com.avispa.microf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements Serializable {
    private Long id;
    private String invoiceDate;
    private String serviceDate;
    private String netValue;
    private String comments;

    public InvoiceDto() {
        super();
    }
}
