package com.avispa.microf.model.customer;

import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.customer.address.AddressDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public abstract class CustomerDto implements Dto {
    public static final String VM_PHONE_PATTERN_NOT_MATCH = "Phone does not match specified pattern";
    public static final String VM_EMAIL_INVALID = "Email address is invalid";
    public static final String VM_EMAIL_NO_LONGER = "The email cannot be longer than 150 characters";
    public static final String VM_ADDRESS_NOT_NULL = "Address cannot be null";

    private UUID id;

    @DisplayName("Customer Name")
    private String objectName;

    @Dictionary(name = "CustomerType")
    @DisplayName("Customer Type")
    private String type;

    @Pattern(regexp = EMPTY_STRING_REGEX + "(\\+48 \\d{9})", message = VM_PHONE_PATTERN_NOT_MATCH)
    @DisplayName("Phone Number")
    private String phoneNumber;

    @Email(message = VM_EMAIL_INVALID)
    @Size(max = 150, message = VM_EMAIL_NO_LONGER)
    @DisplayName("Email")
    private String email;

    @NotNull(message = VM_ADDRESS_NOT_NULL)
    @JsonUnwrapped
    @DisplayName("Address")
    private AddressDto address;
}
