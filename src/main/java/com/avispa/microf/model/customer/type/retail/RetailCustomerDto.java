package com.avispa.microf.model.customer.type.retail;

import com.avispa.microf.model.customer.CustomerDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class RetailCustomerDto extends CustomerDto {
    public static final String VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK = "First name cannot be empty or blank";
    public static final String VM_LAST_NAME_NOT_EMPTY_NOR_BLANK = "Last name cannot be empty or blank";

    @NotBlank(message = VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK)
    private String firstName;

    @NotBlank(message = VM_LAST_NAME_NOT_EMPTY_NOR_BLANK)
    private String lastName;
}
