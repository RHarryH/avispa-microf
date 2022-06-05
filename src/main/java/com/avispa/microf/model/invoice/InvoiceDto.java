package com.avispa.microf.model.invoice;

import com.avispa.microf.model.base.dto.IDto;
import com.avispa.microf.model.invoice.position.PositionDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto implements IDto {
    public static final String VM_SELLER_NOT_NULL = "Seller cannot be null";
    public static final String VM_BUYER_NOT_NULL = "Buyer cannot be null";
    public static final String VM_POSITIONS_NOT_EMPTY = "Positions list cannot be empty";
    public static final String VM_COMMENTS_NO_LONGER = "The comments cannot be longer than 200 characters";

    private UUID id;

    @NotNull(message = VM_SELLER_NOT_NULL)
    private String seller;

    @NotNull(message = VM_BUYER_NOT_NULL)
    private String buyer;

    private String issueDate;
    private String serviceDate;

    @NotEmpty(message = VM_POSITIONS_NOT_EMPTY)
    private List<@Valid PositionDto> positions = new ArrayList<>(1);

    @Size(max = 200, message = VM_COMMENTS_NO_LONGER)
    private String comments;

    public InvoiceDto() {
        this.positions.add(new PositionDto());
    }
}
