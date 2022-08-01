package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.invoice.payment.PaymentDto;
import com.avispa.microf.model.invoice.position.PositionDto;
import lombok.AccessLevel;
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
public class InvoiceDto implements CommonDto {
    public static final String VM_SELLER_NOT_NULL = "Seller cannot be null";
    public static final String VM_BUYER_NOT_NULL = "Buyer cannot be null";
    public static final String VM_POSITIONS_NOT_EMPTY = "Positions list cannot be empty";
    public static final String VM_PAYMENT_NOT_EMPTY = "Payment cannot be empty";
    public static final String VM_COMMENTS_NO_LONGER = "The comments cannot be longer than 200 characters";

    private UUID id;

    @DisplayName("Invoice Name")
    private String objectName;

    @DisplayName("Serial Number")
    private String serialNumber;

    @NotNull(message = VM_SELLER_NOT_NULL)
    @DisplayName("Seller")
    private String seller;

    @NotNull(message = VM_BUYER_NOT_NULL)
    @DisplayName("Buyer")
    private String buyer;

    @DisplayName("Issue Name")
    private String issueDate;

    @DisplayName("Service Date")
    private String serviceDate;

    @NotEmpty(message = VM_POSITIONS_NOT_EMPTY)
    private List<@Valid PositionDto> positions = new ArrayList<>(1);

    @NotNull(message = VM_PAYMENT_NOT_EMPTY)
    private @Valid PaymentDto payment;

    @Size(max = 200, message = VM_COMMENTS_NO_LONGER)
    @DisplayName("Comments")
    private String comments;

    @Getter(AccessLevel.NONE)
    private boolean hasPdfRendition;

    @Override
    public boolean hasPdfRendition() {
        return hasPdfRendition;
    }

    public InvoiceDto() {
        this.positions.add(new PositionDto());
        this.payment = new PaymentDto();
    }
}
