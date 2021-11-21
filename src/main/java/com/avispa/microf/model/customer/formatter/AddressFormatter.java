package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.Address;

/**
 * @author Rafał Hiszpański
 */
public class AddressFormatter {
    public String format(Address address) {
        return address.getStreet() +
                System.getProperty("line.separator") +
                address.getZipCode() + " " + address.getPlace();
    }
}
