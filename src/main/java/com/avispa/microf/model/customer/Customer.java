package com.avispa.microf.model.customer;

import com.avispa.ecm.model.EcmObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Customer extends EcmObject {
    private String phoneNumber; // kept as string because there is no use case to process it as number

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;
}
