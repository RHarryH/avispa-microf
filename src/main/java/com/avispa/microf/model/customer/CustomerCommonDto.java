package com.avispa.microf.model.customer;

import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.base.dto.CommonDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CustomerCommonDto extends CustomerDto implements CommonDto {
    private String companyName;

    @DisplayName("VAT Identification Number")
    private String vatIdentificationNumber;

    private String firstName;
    private String lastName;
}
