package com.avispa.microf.model.customer.type.retail;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class RetailCustomer extends Customer {
    private String firstName;
    private String lastName;

    @Override
    public String format() {
        return firstName + " " + lastName +
                FormatUtils.getNewLine() +
                FormatUtils.getNewLine() +
                getAddress().format();
    }
}
