package com.avispa.microf.model.invoice;

import com.avispa.microf.model.TypedDto;
import com.avispa.microf.model.invoice.position.PositionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements TypedDto<Invoice> {
    private UUID id;
    private UUID seller;
    private UUID buyer;
    private String issueDate;
    private String serviceDate;
    private List<PositionDto> positions = new ArrayList<>(1);
    private String comments;

    public InvoiceDto() {
        this.positions.add(new PositionDto());
    }

    @Override
    public Class<Invoice> getEntityClass() {
        return Invoice.class;
    }
}
