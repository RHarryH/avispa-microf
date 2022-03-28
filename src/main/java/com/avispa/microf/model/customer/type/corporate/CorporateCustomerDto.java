package com.avispa.microf.model.customer.type.corporate;

import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.util.validation.VatIdentificationNumberConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CorporateCustomerDto extends CustomerDto {
    public static final String VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK = "Company name cannot be empty or blank";
    public static final String VM_VIN_PATTERN_NOT_MATCH = "Vat Identification Number does not match specified pattern";

    @NotBlank(message = VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK)
    private String companyName;

    @VatIdentificationNumberConstraint
    @Pattern(regexp = "[0-9]{3}-[0-9]{2}-[0-9]{2}-[0-9]{3}", message = VM_VIN_PATTERN_NOT_MATCH)
    private String vatIdentificationNumber;
}
