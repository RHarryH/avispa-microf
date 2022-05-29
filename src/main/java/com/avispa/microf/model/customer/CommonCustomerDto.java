package com.avispa.microf.model.customer;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CommonCustomerDto extends CustomerDto {
    private String companyName;
    private String vatIdentificationNumber;

    private String firstName;
    private String lastName;
}
