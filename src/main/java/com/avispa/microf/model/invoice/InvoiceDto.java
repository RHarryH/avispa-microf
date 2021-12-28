package com.avispa.microf.model.invoice;

import com.avispa.microf.model.Dto;
import com.avispa.microf.model.invoice.position.PositionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements Dto {
    private UUID id;
    private UUID seller;
    private UUID buyer;
    private String invoiceDate;
    private String serviceDate;
    private PositionDto[] positions;
    private String comments;
}
