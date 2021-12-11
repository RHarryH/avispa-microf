package com.avispa.microf.model.customer.service;

import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ContextService contextService;

    @Transactional
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }
}
