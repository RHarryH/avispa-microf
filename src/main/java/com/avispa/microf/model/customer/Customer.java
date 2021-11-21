package com.avispa.microf.model.customer;

import com.avispa.ecm.model.EcmObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Customer extends EcmObject {
    private boolean retail;

    // retail customer
    private String firstName;
    private String lastName;

    // corporate customer
    private String companyName;
    private String vatIdentificationNumber; // in Poland Numer Identyfikacji Podatkowej (NIP)

    private String phoneNumber; // kept as string because there is no use case to process it as number

    @ManyToOne
    private Address address;
}
