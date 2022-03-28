package com.avispa.microf.model.customer.type.corporate;

import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.customer.Customer;
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
public class CorporateCustomerService implements BaseService<CorporateCustomer, CorporateCustomerDto> {
    private final CustomerRepository customerRepository;
    private final CorporateCustomerMapper corporateCustomerMapper;
    private final ContextService contextService;

    @Transactional
    @Override
    public void add(CorporateCustomer customer) {
        customerRepository.save(customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Transactional
    @Override
    public void update(CorporateCustomerDto customerDto) {
        CorporateCustomer customer = findById(customerDto.getId());
        corporateCustomerMapper.updateEntityFromDto(customerDto, customer);

        contextService.applyMatchingConfigurations(customer, Autoname.class);
    }

    @Override
    public void delete(UUID id) {
        customerRepository.delete(findById(id));
    }

    @Override
    public CorporateCustomer findById(UUID id) {
        return (CorporateCustomer) customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Customer.class));
    }
}
