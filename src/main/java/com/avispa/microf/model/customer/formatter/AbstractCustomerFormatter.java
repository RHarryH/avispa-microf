package com.avispa.microf.model.customer.formatter;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractCustomerFormatter implements CustomerFormatter {
    protected AddressFormatter addressFormatter = new AddressFormatter();

    protected String addNewLine() {
        return System.getProperty("line.separator");
    }
}
