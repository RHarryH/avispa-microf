package com.avispa.microf.model.bankaccount;

import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BankAccountService implements BaseService<BankAccount, BankAccountDto> {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Transactional
    @Override
    public void add(BankAccount customer) {
        bankAccountRepository.save(customer);
    }

    @Transactional
    @Override
    public void update(BankAccountDto customerDto) {
        BankAccount bankAccount = findById(customerDto.getId());
        bankAccountMapper.updateEntityFromDto(customerDto, bankAccount);
    }

    @Override
    public void delete(UUID id) {
        bankAccountRepository.delete(findById(id));
    }

    @Override
    public BankAccount findById(UUID id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BankAccount.class));
    }

    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }
}
