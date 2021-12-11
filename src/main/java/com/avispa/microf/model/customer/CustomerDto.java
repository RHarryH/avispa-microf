package com.avispa.microf.model.customer;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public abstract class CustomerDto implements Dto {
    private String phoneNumber;

    // address
    private String street;
    private String place;
    private String zipCode;
}
