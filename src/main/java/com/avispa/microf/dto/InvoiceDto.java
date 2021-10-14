package com.avispa.microf.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements Serializable {
    private UUID id;
    private String invoiceDate;
    private String serviceDate;
    private String netValue;
    private String comments;
}
