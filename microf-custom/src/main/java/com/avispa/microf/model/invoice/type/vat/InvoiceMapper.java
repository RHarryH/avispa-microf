/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice.type.vat;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.invoice.AbstractInvoiceMapper;
import com.avispa.microf.model.invoice.payment.PaymentMapper;
import com.avispa.microf.model.invoice.position.PositionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapper.class, PaymentMapper.class})
public abstract class InvoiceMapper extends AbstractInvoiceMapper<Invoice, InvoiceDto> {
    @Autowired
    private CustomerRepository customerRepository;

    protected String customerToId(Customer customer) {
        return customer.getId().toString();
    }

    protected Customer idToCustomer(String customerId) {
        return customerRepository.getReferenceById(UUID.fromString(customerId));
    }
}