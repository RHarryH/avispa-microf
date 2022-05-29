package com.avispa.microf.model.customer.corporate;

import com.avispa.microf.model.customer.address.AddressDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class CorporateCustomerDtoValidationTest {
    private CorporateCustomerDto customerDto;

    @BeforeEach
    void createDto() {
        customerDto = new CorporateCustomerDto();
        customerDto.setCompanyName("First");
        customerDto.setVatIdentificationNumber("111-11-11-111");
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
        customerDto.setCompanyName("");
        validate(customerDto, CorporateCustomerDto.VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankFirstName_whenValidate_thenFail() {
        customerDto.setCompanyName("   \n\t");
        validate(customerDto, CorporateCustomerDto.VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenIncorrectVIN_whenValidate_thenFail() {
        customerDto.setVatIdentificationNumber("123-123-1");
        validate(customerDto, 2, Set.of(CorporateCustomerDto.VM_VIN_PATTERN_NOT_MATCH, "Vat Identification Number is invalid"));
    }

    @Test
    void givenIncorrectVINControlSum_whenValidate_thenFail() { // incorrect control sum
        customerDto.setVatIdentificationNumber("111-11-11-112");
        validate(customerDto, "Vat Identification Number is invalid");
    }
}