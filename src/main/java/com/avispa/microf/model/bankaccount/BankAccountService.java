package com.avispa.microf.model.bankaccount;

import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.error.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Component
@Slf4j
public class BankAccountService extends BaseService<BankAccount, BankAccountDto, BankAccountRepository, BankAccountMapper> {

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, BankAccountMapper entityDtoMapper) {
        super(bankAccountRepository, entityDtoMapper);
    }

    @Transactional
    @Override
    public void add(BankAccount customer) {
        getRepository().save(customer);
    }

    @Transactional
    @Override
    public void update(BankAccountDto customerDto) {
        BankAccount bankAccount = findById(customerDto.getId());
        getEntityDtoMapper().updateEntityFromDto(customerDto, bankAccount);
    }

    @Override
    public void delete(UUID id) {
        getRepository().delete(findById(id));
    }

    @Override
    public BankAccount findById(UUID id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BankAccount.class));
    }
}
