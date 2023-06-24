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

import com.avispa.ecm.model.base.mapper.MultiTypeEntityDtoMapper;
import com.avispa.ecm.model.base.mapper.MultiTypeMapperRegistry;
import com.avispa.microf.model.customer.corporate.CorporateCustomerMapper;
import com.avispa.microf.model.customer.retail.RetailCustomerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CustomerMapper extends MultiTypeEntityDtoMapper<Customer, CustomerDto> {
    @Autowired
    private CorporateCustomerMapper corporateCustomerMapper;

    @Autowired
    private RetailCustomerMapper retailCustomerMapper;

    @Override
    protected void registerMappers(MultiTypeMapperRegistry multiTypeMapperRegistry) {
        multiTypeMapperRegistry.registerMapper("CORPORATE", corporateCustomerMapper);
        multiTypeMapperRegistry.registerMapper("RETAIL", retailCustomerMapper);
    }

    @Override
    protected String getDiscriminator(Customer entity) {
        return entity.getType();
    }

    @Override
    protected String getDiscriminator(CustomerDto dto) {
        return dto.getType();
    }
}