package com.avispa.microf.model.customer;

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

    public String format() {
        return street +
                System.getProperty("line.separator") +
                zipCode + " " + place;
    }
}
