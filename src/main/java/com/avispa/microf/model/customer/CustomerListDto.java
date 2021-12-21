package com.avispa.microf.model.customer;

import com.avispa.microf.model.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Dto used to display customers in Customer List widget
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CustomerListDto implements Dto {
    private UUID id;
    private String customerName;
    private boolean corporate;
    private String vatIdentificationNumber;
    private String phoneNumber;
    private String email;
    private String address;
}
