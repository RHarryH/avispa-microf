package com.avispa.microf.model.customer.type.retail;

import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.customer.CustomerRepository;
import com.avispa.microf.model.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RetailCustomerService implements BaseService<RetailCustomer, RetailCustomerDto> {
    private final CustomerRepository customerRepository;
    private final RetailCustomerMapper retailCustomerMapper;
    private final ContextService contextService;

    @Transactional
    @Override
    public void add(RetailCustomer customer) {
        customerRepository.save(customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Transactional
    @Override
    public void update(RetailCustomerDto customerDto) {
        RetailCustomer customer = findById(customerDto.getId());
        retailCustomerMapper.updateEntityFromDto(customerDto, customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Override
    public void delete(UUID id) {
        customerRepository.delete(findById(id));
    }

    @Override
    public RetailCustomer findById(UUID id) {
        return (RetailCustomer) customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RetailCustomer.class));
    }
}
