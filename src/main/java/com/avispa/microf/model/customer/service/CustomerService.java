package com.avispa.microf.model.customer.service;

import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.model.customer.CustomerListDto;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.customer.exception.CustomerNotFoundException;
import com.avispa.microf.model.customer.mapper.CustomerListMapper;
import com.avispa.microf.model.customer.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerListMapper customerListMapper;
    private final CustomerMapper customerMapper;
    private final ContextService contextService;

    @Transactional
    public void add(Customer customer) {
        customerRepository.save(customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Transactional
    public void update(CustomerDto customerDto) {
        Customer customer = findById(customerDto.getId());
        customerMapper.updateCustomerFromDto(customerDto, customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    public void delete(UUID id) {
        customerRepository.delete(findById(id));
    }

    public Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
    }

    public List<CustomerListDto> findAll() {
        return customerRepository.findAll().stream().map(customerListMapper::toCustomerListDto).collect(Collectors.toList());
    }
}
