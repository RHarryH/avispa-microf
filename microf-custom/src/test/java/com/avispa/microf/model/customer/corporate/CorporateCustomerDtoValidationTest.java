/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

        CorporateCustomerDetailDto customerDetailDto = new CorporateCustomerDetailDto();
        customerDetailDto.setCompanyName("Company");
        customerDetailDto.setVatIdentificationNumber("111-11-11-111");
        customerDto.setDetails(customerDetailDto);

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
        customerDto.getDetails().setCompanyName("");
        validate(customerDto, CorporateCustomerDetailDto.VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankFirstName_whenValidate_thenFail() {
        customerDto.getDetails().setCompanyName("   \n\t");
        validate(customerDto, CorporateCustomerDetailDto.VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenIncorrectVIN_whenValidate_thenFail() {
        customerDto.getDetails().setVatIdentificationNumber("123-123-1");
        validate(customerDto, Set.of(CorporateCustomerDetailDto.VM_VIN_PATTERN_NOT_MATCH, "VAT Identification Number is invalid"));
    }

    @Test
    void givenIncorrectVINControlSum_whenValidate_thenFail() { // incorrect control sum
        customerDto.getDetails().setVatIdentificationNumber("111-11-11-112");
        validate(customerDto, "VAT Identification Number is invalid");
    }
}