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
class CorporateCustomerMapperTest {
    @Autowired
    private CorporateCustomerMapper mapper;

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        Customer customer = getSampleEntity();
        CorporateCustomerDto convertedDto = mapper.convertToDto(customer);

        AddressDto address = convertedDto.getAddress();
        assertAll(() -> {
            assertEquals("Customer", convertedDto.getDetails().getCompanyName());
            assertEquals("222-11-11-111", convertedDto.getDetails().getVatIdentificationNumber());
            assertEquals("old@mail.com", convertedDto.getEmail());
            assertEquals("+48 456456456", convertedDto.getPhoneNumber());
            assertEquals("Old Street", address.getStreet());
            assertEquals("Old Place", address.getPlace());
            assertEquals("67-890", address.getZipCode());
        });
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        CorporateCustomerDto customerDto = getSampleDto();
        Customer convertedEntity = mapper.convertToEntity(customerDto);

        Address address = convertedEntity.getAddress();
        assertAll(() -> {
            assertNull(convertedEntity.getFirstName());
            assertNull(convertedEntity.getLastName());
            assertEquals("Company", convertedEntity.getCompanyName());
            assertEquals("111-11-11-111", convertedEntity.getVatIdentificationNumber());
            assertEquals("email@mail.com", convertedEntity.getEmail());
            assertEquals("+48 123123123", convertedEntity.getPhoneNumber());
            assertEquals("Street", address.getStreet());
            assertEquals("Place", address.getPlace());
            assertEquals("12-235", address.getZipCode());
        });
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        Customer customer = getSampleEntity();
        CorporateCustomerDto customerDto = getSampleDto();
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

    @Test
    void givenDtoAndEmptyEntity_whenUpdate_thenEntityHasDtoProperties() {
        Customer customer = new Customer();
        CorporateCustomerDto customerDto = getSampleDto();
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

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        Customer customer = getSampleEntity();
        mapper.updateEntityFromDto(null, customer);

        Address address = customer.getAddress();
        assertAll(() -> {
            assertNull(customer.getFirstName());
            assertNull(customer.getLastName());
            assertEquals("Customer", customer.getCompanyName());
            assertEquals("222-11-11-111", customer.getVatIdentificationNumber());
            assertEquals("old@mail.com", customer.getEmail());
            assertEquals("+48 456456456", customer.getPhoneNumber());
            assertEquals("Old Street", address.getStreet());
            assertEquals("Old Place", address.getPlace());
            assertEquals("67-890", address.getZipCode());
        });
    }

    private Customer getSampleEntity() {
        Customer customer = new Customer();
        customer.setCompanyName("Customer");
        customer.setVatIdentificationNumber("222-11-11-111");
        customer.setEmail("old@mail.com");
        customer.setPhoneNumber("+48 456456456");

        Address address = new Address();
        address.setStreet("Old Street");
        address.setPlace("Old Place");
        address.setZipCode("67-890");

        customer.setAddress(address);

        return customer;
    }

    private CorporateCustomerDto getSampleDto() {
        CorporateCustomerDto customerDto = new CorporateCustomerDto();

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

        return customerDto;
    }
}