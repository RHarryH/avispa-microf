package com.avispa.microf.model.customer;

import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.error.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Service
public class CustomerService extends BaseService<Customer, CustomerDto, CustomerMapper> {
    private final CustomerRepository customerRepository;
    private final CustomerListMapper customerListMapper;
    private final ContextService contextService;

    public CustomerService(CustomerMapper customerMapper, CustomerRepository customerRepository, CustomerListMapper customerListMapper, ContextService contextService) {
        super(customerMapper);
        this.customerRepository = customerRepository;
        this.customerListMapper = customerListMapper;
        this.contextService = contextService;
    }

    @Transactional
    @Override
    public void add(Customer customer) {
        customerRepository.save(customer);

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
        customerRepository.delete(findById(id));
    }

    @Override
    public Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Customer.class));
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
