package com.avispa.microf.model.invoice.position;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

/**
 * Position on the invoice. Position name is object name.
 *
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PositionDto implements Dto {
    private String objectName;
    private String quantity = "1";
    private String unit;
    private String unitPrice;
    private String discount = "0.00";
    private String vatRate;
}
