package com.avispa.microf.model.customer;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.TypeDiscriminator;
import com.avispa.microf.model.customer.address.Address;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
@TypeDiscriminator(name = "type")
public class Customer extends EcmObject {
    private String phoneNumber; // kept as string because there is no use case to process it as number

    @Column(length = 150)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    private String type;

    // corporate
    private String companyName;

    @Column(length = 13)
    private String vatIdentificationNumber; // in Poland Numer Identyfikacji Podatkowej (NIP)

    // retail
    private String firstName;
    private String lastName;

    public String format() {
        if(null == type) {
            throw new IllegalStateException("Customer type not specified");
        }

        if(type.equals("CORPORATE")) {
            return corporateFormat();
        } else if(type.equals("RETAIL")) {
            return retailFormat();
        } else {
            return "Unknown Customer type";
        }
    }

    private String corporateFormat() {
        return companyName +
                FormatUtils.getNewLine() +
                FormatUtils.getNewLine() +
                getAddress().format() +
                FormatUtils.getNewLine() +
                "NIP: " + vatIdentificationNumber;
    }

    private String retailFormat() {
        return firstName + " " + lastName +
                FormatUtils.getNewLine() +
                FormatUtils.getNewLine() +
                getAddress().format();
    }
}
