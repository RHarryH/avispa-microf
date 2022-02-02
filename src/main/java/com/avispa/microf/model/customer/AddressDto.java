package com.avispa.microf.model.customer;

import com.avispa.microf.model.TypedDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class AddressDto implements TypedDto<Address> {
    private String street;
    private String place;
    private String zipCode;

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
}
