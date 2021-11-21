package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.Customer;

/**
 * @author Rafał Hiszpański
 */
public class CorporateCustomerFormatter extends AbstractCustomerFormatter {
    @Override
    public String format(Customer customer) {
        return customer.getCompanyName() +
                addNewLine() +
                addNewLine() +
                addressFormatter.format(customer.getAddress()) +
                addNewLine() +
                customer.getVatIdentificationNumber();
    }
}
