package com.avispa.microf.model.customer;

import com.avispa.microf.model.customer.mapper.CustomerListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerListMapper customerListMapper;

    public List<CustomerListDto> findAll() {
        return customerRepository.findAll().stream().map(customerListMapper::toCustomerListDto).collect(Collectors.toList());
    }
}
