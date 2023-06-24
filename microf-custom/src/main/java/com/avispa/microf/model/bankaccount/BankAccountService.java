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

package com.avispa.microf.model.bankaccount;

import com.avispa.ecm.model.base.BaseService;
import com.avispa.ecm.model.error.ResourceNotFoundException;
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
