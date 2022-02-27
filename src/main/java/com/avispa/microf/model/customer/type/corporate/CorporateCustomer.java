package com.avispa.microf.model.customer.type.corporate;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.util.FormatUtils;
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
public class CorporateCustomer extends Customer {
    private String companyName;

    @Column(length = 10)
    private String vatIdentificationNumber; // in Poland Numer Identyfikacji Podatkowej (NIP)

    @Override
    public String format() {
        return companyName +
                FormatUtils.getNewLine() +
                FormatUtils.getNewLine() +
                getAddress().format() +
                FormatUtils.getNewLine() +
                "NIP: " + vatIdentificationNumber;
    }
}
