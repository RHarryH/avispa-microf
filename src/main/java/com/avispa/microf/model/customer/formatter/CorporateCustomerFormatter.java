package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;

/**
 * @author Rafał Hiszpański
 */
public class CorporateCustomerFormatter extends AbstractCustomerFormatter<CorporateCustomer> {
    @Override
    public String format(CorporateCustomer customer) {
        return customer.getCompanyName() +
                addNewLine() +
                addNewLine() +
                addressFormatter.format(customer.getAddress()) +
                addNewLine() +
                "NIP: " + customer.getVatIdentificationNumber();
    }
}
