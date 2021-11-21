package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.Customer;

/**
 * @author Rafał Hiszpański
 */
public class RetailCustomerFormatter extends AbstractCustomerFormatter {
    @Override
    public String format(Customer customer) {
        return customer.getFirstName() + " " + customer.getLastName() +
                addNewLine() +
                addNewLine() +
                addressFormatter.format(customer.getAddress());
    }
}
