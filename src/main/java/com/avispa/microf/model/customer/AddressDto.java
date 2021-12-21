package com.avispa.microf.model.customer;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class AddressDto implements Dto {
    private String street;
    private String place;
    private String zipCode;
}
