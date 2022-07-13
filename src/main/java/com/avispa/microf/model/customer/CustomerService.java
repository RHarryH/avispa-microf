package com.avispa.microf.model.customer;

import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.error.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Service
public class CustomerService extends BaseService<Customer, CustomerDto, CustomerRepository, CustomerMapper> {
    private final ContextService contextService;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper,
                           ContextService contextService) {
        super(customerRepository, customerMapper);
        this.contextService = contextService;
    }

    @Transactional
    @Override
    public void add(Customer customer) {
        getRepository().save(customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Transactional
    @Override
    public void update(CustomerDto customerDto) {
        Customer customer = findById(customerDto.getId());
        getEntityDtoMapper().updateEntityFromDto(customerDto, customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Override
    public void delete(UUID id) {
        getRepository().delete(findById(id));
    }

    @Override
    public Customer findById(UUID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Customer.class));
    }
}
