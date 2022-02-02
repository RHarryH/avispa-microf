package com.avispa.microf.model.customer.type.corporate;

import com.avispa.microf.model.customer.CustomerDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CorporateCustomerDto extends CustomerDto<CorporateCustomer> {
    private String companyName;
    private String vatIdentificationNumber;

    @Override
    public Class<CorporateCustomer> getEntityClass() {
        return CorporateCustomer.class;
    }
}
