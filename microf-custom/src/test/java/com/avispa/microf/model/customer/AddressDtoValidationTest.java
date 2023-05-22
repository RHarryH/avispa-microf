package com.avispa.microf.model.customer;

import com.avispa.microf.model.customer.address.AddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class AddressDtoValidationTest {
    private AddressDto addressDto;

    @BeforeEach
    void createDto() {
        addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setPlace("Place");
        addressDto.setZipCode("12-235");
    }

    @Test
    void givenEmptyStreet_whenValidate_thenFail() {
        addressDto.setStreet("");
        validate(addressDto, AddressDto.VM_STREET_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankStreet_whenValidate_thenFail() {
        addressDto.setStreet("   \n\t");
        validate(addressDto, AddressDto.VM_STREET_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenEmptyPlace_whenValidate_thenFail() {
        addressDto.setPlace("");
        validate(addressDto, AddressDto.VM_PLACE_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankPlace_whenValidate_thenFail() {
        addressDto.setPlace("   \n\t");
        validate(addressDto, AddressDto.VM_PLACE_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenIncorrectZipCode_whenValidate_thenFail() {
        addressDto.setZipCode("ab-2-5");
        validate(addressDto, AddressDto.VM_ZIP_CODE_PATTERN_NOT_MATCH);
    }
}