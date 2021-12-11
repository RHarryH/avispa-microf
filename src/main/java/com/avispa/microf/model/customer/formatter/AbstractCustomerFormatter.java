package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.Customer;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractCustomerFormatter<C extends Customer> implements CustomerFormatter<C> {
    protected AddressFormatter addressFormatter = new AddressFormatter();

    protected String addNewLine() {
        return System.getProperty("line.separator");
    }
}
