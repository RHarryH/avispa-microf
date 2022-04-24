package com.avispa.microf.model.customer;

import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.customer.address.AddressDto;
import com.avispa.microf.util.validation.VatIdentificationNumberConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CustomerDto implements Dto {
    public static final String VM_PHONE_PATTERN_NOT_MATCH = "Phone does not match specified pattern";
    public static final String VM_EMAIL_INVALID = "Email address is invalid";
    public static final String VM_EMAIL_NO_LONGER = "The email cannot be longer than 150 characters";
    public static final String VM_ADDRESS_NOT_NULL = "Address cannot be null";

    // corporate
    public static final String VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK = "Company name cannot be empty or blank";
    public static final String VM_VIN_PATTERN_NOT_MATCH = "Vat Identification Number does not match specified pattern";

    // retail
    public static final String VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK = "First name cannot be empty or blank";
    public static final String VM_LAST_NAME_NOT_EMPTY_NOR_BLANK = "Last name cannot be empty or blank";

    private UUID id;

    @Dictionary(name = "CustomerType")
    private String type;

    @Pattern(regexp = EMPTY_STRING_REGEX + "(\\+48 \\d{9})", message = VM_PHONE_PATTERN_NOT_MATCH)
    private String phoneNumber;

    @Email(message = VM_EMAIL_INVALID)
    @Size(max = 150, message = VM_EMAIL_NO_LONGER)
    private String email;

    @NotNull(message = VM_ADDRESS_NOT_NULL)
    private AddressDto address;

    // corporate
    @NotBlank(message = VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK)
    private String companyName;

    @VatIdentificationNumberConstraint
    @Pattern(regexp = "[0-9]{3}-[0-9]{2}-[0-9]{2}-[0-9]{3}", message = VM_VIN_PATTERN_NOT_MATCH)
    private String vatIdentificationNumber;

    // retail
    @NotBlank(message = VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK)
    private String firstName;

    @NotBlank(message = VM_LAST_NAME_NOT_EMPTY_NOR_BLANK)
    private String lastName;
}
