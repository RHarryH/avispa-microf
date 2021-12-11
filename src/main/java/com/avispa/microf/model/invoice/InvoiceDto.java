package com.avispa.microf.model.invoice;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements Serializable, Dto {
    private UUID id;
    private UUID seller;
    private UUID buyer;
    private String invoiceDate;
    private String serviceDate;
    private String netValue;
    private String comments;
}
