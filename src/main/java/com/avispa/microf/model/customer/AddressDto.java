package com.avispa.microf.model.customer;

import com.avispa.microf.model.TypedDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class AddressDto implements TypedDto<Address> {
    public static final String VM_STREET_NOT_EMPTY_NOR_BLANK = "Street cannot be empty or blank";
    public static final String VM_PLACE_NOT_EMPTY_NOR_BLANK = "Place cannot be empty or blank";
    public static final String VM_ZIP_CODE_PATTERN_NOT_MATCH = "Zip code does not match specified pattern";

    @NotBlank(message = VM_STREET_NOT_EMPTY_NOR_BLANK)
    private String street;

    @NotBlank(message = VM_PLACE_NOT_EMPTY_NOR_BLANK)
    private String place;

    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = VM_ZIP_CODE_PATTERN_NOT_MATCH)
    private String zipCode;

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
}
