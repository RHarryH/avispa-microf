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

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.Address;
import com.avispa.microf.model.customer.address.AddressDto;
import com.avispa.microf.model.customer.address.AddressMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CorporateCustomerMapperImpl.class, AddressMapperImpl.class})
class CorporateCustomerDtoTest {
    private CorporateCustomerDto customerDto;
    private Customer customer;

    @Autowired
    private CorporateCustomerMapper mapper;

    @BeforeEach
    void createDto() {
        // dto
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

        // entity
        customer = new Customer();
        customer.setCompanyName("Customer");
        customer.setVatIdentificationNumber("222-11-11-111");
        customer.setEmail("old@mail.com");
        customer.setPhoneNumber("+48 456456456");

        Address address = new Address();
        address.setStreet("Old Street");
        address.setPlace("Old Place");
        address.setZipCode("67-890");

        customer.setAddress(address);
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        mapper.updateEntityFromDto(customerDto, customer);

        Address address = customer.getAddress();
        assertAll(() -> {
            assertNull(customer.getFirstName());
            assertNull(customer.getLastName());
            assertEquals("Company", customer.getCompanyName());
            assertEquals("111-11-11-111", customer.getVatIdentificationNumber());
            assertEquals("email@mail.com", customer.getEmail());
            assertEquals("+48 123123123", customer.getPhoneNumber());
            assertEquals("Street", address.getStreet());
            assertEquals("Place", address.getPlace());
            assertEquals("12-235", address.getZipCode());
        });
    }
}