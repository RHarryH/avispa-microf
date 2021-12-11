package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.type.retail.RetailCustomer;

/**
 * @author Rafał Hiszpański
 */
public class RetailCustomerFormatter extends AbstractCustomerFormatter<RetailCustomer> {
    @Override
    public String format(RetailCustomer customer) {
        return customer.getFirstName() + " " + customer.getLastName() +
                addNewLine() +
                addNewLine() +
                addressFormatter.format(customer.getAddress());
    }
}
