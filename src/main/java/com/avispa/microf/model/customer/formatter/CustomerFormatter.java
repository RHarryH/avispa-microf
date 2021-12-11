package com.avispa.microf.model.customer.formatter;

import com.avispa.microf.model.customer.Customer;

/**
 * @author Rafał Hiszpański
 */
public interface CustomerFormatter<C extends Customer> {
    String format(C customer);
}
