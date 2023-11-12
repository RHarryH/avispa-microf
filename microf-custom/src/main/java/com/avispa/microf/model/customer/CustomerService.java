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

import com.avispa.ecm.model.base.BaseEcmService;
import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.util.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Service
public class CustomerService extends BaseEcmService<Customer, CustomerDto, CustomerRepository, CustomerMapper> {
    private final ContextService contextService;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper,
                           ContextService contextService) {
        super(customerRepository, customerMapper);
        this.contextService = contextService;
    }

    @Override
    protected void add(Customer customer) {
        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Override
    protected void update(Customer customer) {
        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Override
    public Customer findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Customer.class));
    }
}
