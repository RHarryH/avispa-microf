package com.avispa.microf.model.customer;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public abstract class CustomerDto implements Dto {
    private UUID id;
    private String phoneNumber;
    private String email;

    private AddressDto address;
}
