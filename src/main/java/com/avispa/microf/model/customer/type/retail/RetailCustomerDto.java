package com.avispa.microf.model.customer.type.retail;

import com.avispa.microf.model.customer.CustomerDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class RetailCustomerDto extends CustomerDto<RetailCustomer> {
    private String firstName;
    private String lastName;

    @Override
    public Class<RetailCustomer> getEntityClass() {
        return RetailCustomer.class;
    }
}
