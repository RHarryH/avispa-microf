package com.avispa.microf.model.customer.retail;

import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.model.customer.address.AddressDto;
import com.avispa.microf.model.customer.retail.RetailCustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class RetailCustomerDtoValidationTest {
    private RetailCustomerDto customerDto;

    @BeforeEach
    void createDto() {
        customerDto = new RetailCustomerDto();
        customerDto.setFirstName("First");
        customerDto.setLastName("Last");
        customerDto.setEmail("email@mail.com");
        customerDto.setPhoneNumber("+48 123123123");

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setPlace("Place");
        addressDto.setZipCode("12-235");

        customerDto.setAddress(addressDto);
    }

    @Test
    void givenEmptyFirstName_whenValidate_thenFail() {
        customerDto.setFirstName("");
        validate(customerDto, RetailCustomerDto.VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankFirstName_whenValidate_thenFail() {
        customerDto.setFirstName("   \n\t");
        validate(customerDto, RetailCustomerDto.VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenEmptyLastName_whenValidate_thenFail() {
        customerDto.setLastName("");
        validate(customerDto, RetailCustomerDto.VM_LAST_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankLastName_whenValidate_thenFail() {
        customerDto.setLastName("   \n\t");
        validate(customerDto, RetailCustomerDto.VM_LAST_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenIncorrectEmail_whenValidate_thenFail() {
        customerDto.setEmail("Invalid mail");
        validate(customerDto, CustomerDto.VM_EMAIL_INVALID);
    }

    @Test
    void givenIncorrectPhoneNumber_whenValidate_thenFail() {
        customerDto.setPhoneNumber("123c");
        validate(customerDto, CustomerDto.VM_PHONE_PATTERN_NOT_MATCH);
    }

    @Test
    void givenEmptyPhoneNumber_whenValidate_thenDontFail() {
        customerDto.setPhoneNumber("");
        validate(customerDto, Collections.emptySet());
    }

    @Test
    void givenNullAddress_whenValidate_thenFail() {
        customerDto.setAddress(null);
        validate(customerDto, CustomerDto.VM_ADDRESS_NOT_NULL);
    }
}