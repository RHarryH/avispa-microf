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

package com.avispa.microf.model.customer.retail;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.Address;
import com.avispa.microf.model.customer.address.AddressDto;
import com.avispa.microf.model.customer.address.AddressMapperImpl;
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
@ContextConfiguration(classes = {RetailCustomerMapperImpl.class, AddressMapperImpl.class})
class RetailCustomerMapperTest {

    @Autowired
    private RetailCustomerMapper mapper;

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        Customer customer = getSampleEntity();
        RetailCustomerDto convertedDto = mapper.convertToDto(customer);

        AddressDto address = convertedDto.getAddress();
        assertAll(
                () -> assertEquals("John", convertedDto.getDetails().getFirstName()),
                () -> assertEquals("Doe", convertedDto.getDetails().getLastName()),
                () -> assertEquals("old@mail.com", convertedDto.getEmail()),
                () -> assertEquals("+48 456456456", convertedDto.getPhoneNumber()),
                () -> assertEquals("Old Street", address.getStreet()),
                () -> assertEquals("Old Place", address.getPlace()),
                () -> assertEquals("67-890", address.getZipCode())
        );
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        RetailCustomerDto customerDto = getSampleDto();
        Customer convertedEntity = mapper.convertToEntity(customerDto);

        Address address = convertedEntity.getAddress();
        assertAll(
                () -> assertEquals("First", convertedEntity.getFirstName()),
                () -> assertEquals("Last", convertedEntity.getLastName()),
                () -> assertNull(convertedEntity.getCompanyName()),
                () -> assertNull(convertedEntity.getVatIdentificationNumber()),
                () -> assertEquals("email@mail.com", convertedEntity.getEmail()),
                () -> assertEquals("+48 123123123", convertedEntity.getPhoneNumber()),
                () -> assertEquals("Street", address.getStreet()),
                () -> assertEquals("Place", address.getPlace()),
                () -> assertEquals("12-235", address.getZipCode())
        );
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        Customer customer = getSampleEntity();
        RetailCustomerDto customerDto = getSampleDto();
        mapper.updateEntityFromDto(customerDto, customer);

        Address address = customer.getAddress();
        assertAll(
                () -> assertEquals("First", customer.getFirstName()),
                () -> assertEquals("Last", customer.getLastName()),
                () -> assertNull(customer.getCompanyName()),
                () -> assertNull(customer.getVatIdentificationNumber()),
                () -> assertEquals("email@mail.com", customer.getEmail()),
                () -> assertEquals("+48 123123123", customer.getPhoneNumber()),
                () -> assertEquals("Street", address.getStreet()),
                () -> assertEquals("Place", address.getPlace()),
                () -> assertEquals("12-235", address.getZipCode())
        );
    }

    @Test
    void givenDtoAndNewEntity_whenUpdate_thenEntityHasDtoProperties() {
        Customer customer = new Customer();
        RetailCustomerDto customerDto = getSampleDto();
        mapper.updateEntityFromDto(customerDto, customer);

        Address address = customer.getAddress();
        assertAll(
                () -> assertEquals("First", customer.getFirstName()),
                () -> assertEquals("Last", customer.getLastName()),
                () -> assertNull(customer.getCompanyName()),
                () -> assertNull(customer.getVatIdentificationNumber()),
                () -> assertEquals("email@mail.com", customer.getEmail()),
                () -> assertEquals("+48 123123123", customer.getPhoneNumber()),
                () -> assertEquals("Street", address.getStreet()),
                () -> assertEquals("Place", address.getPlace()),
                () -> assertEquals("12-235", address.getZipCode())
        );
    }

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        Customer customer = getSampleEntity();
        mapper.updateEntityFromDto(null, customer);

        Address address = customer.getAddress();
        assertAll(
                () -> assertEquals("John", customer.getFirstName()),
                () -> assertEquals("Doe", customer.getLastName()),
                () -> assertNull(customer.getCompanyName()),
                () -> assertNull(customer.getVatIdentificationNumber()),
                () -> assertEquals("old@mail.com", customer.getEmail()),
                () -> assertEquals("+48 456456456", customer.getPhoneNumber()),
                () -> assertEquals("Old Street", address.getStreet()),
                () -> assertEquals("Old Place", address.getPlace()),
                () -> assertEquals("67-890", address.getZipCode())
        );
    }

    private Customer getSampleEntity() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("old@mail.com");
        customer.setPhoneNumber("+48 456456456");

        Address address = new Address();
        address.setStreet("Old Street");
        address.setPlace("Old Place");
        address.setZipCode("67-890");

        customer.setAddress(address);

        return customer;
    }

    private RetailCustomerDto getSampleDto() {
        RetailCustomerDto customerDto = new RetailCustomerDto();

        RetailCustomerDetailDto customerDetailDto = new RetailCustomerDetailDto();
        customerDetailDto.setFirstName("First");
        customerDetailDto.setLastName("Last");
        customerDto.setDetails(customerDetailDto);

        customerDto.setEmail("email@mail.com");
        customerDto.setPhoneNumber("+48 123123123");

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setPlace("Place");
        addressDto.setZipCode("12-235");

        customerDto.setAddress(addressDto);

        return customerDto;
    }
}