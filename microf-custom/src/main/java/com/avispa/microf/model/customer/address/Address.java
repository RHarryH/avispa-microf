package com.avispa.microf.model.customer.address;

import com.avispa.ecm.model.EcmObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Address extends EcmObject {

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false, length = 6)
    private String zipCode;

    @Override
    public String toString() {
        return street +
                System.getProperty("line.separator") +
                zipCode + " " + place;
    }
}
