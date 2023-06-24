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