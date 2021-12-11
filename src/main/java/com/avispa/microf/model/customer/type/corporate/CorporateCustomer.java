package com.avispa.microf.model.customer.type.corporate;

import com.avispa.microf.model.customer.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class CorporateCustomer extends Customer {
    private String companyName;
    private String vatIdentificationNumber; // in Poland Numer Identyfikacji Podatkowej (NIP)
}
