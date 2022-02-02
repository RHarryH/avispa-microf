package com.avispa.microf.model.customer;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.TypedDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public abstract class CustomerDto<T extends EcmObject> implements TypedDto<T> {
    private UUID id;
    private String phoneNumber;
    private String email;

    private AddressDto address;
}
