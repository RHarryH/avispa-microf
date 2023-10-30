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

package com.avispa.microf.model.invoice.payment;

import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountRepository;
import com.avispa.ecm.model.base.mapper.EntityDtoMapper;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class PaymentMapper implements EntityDtoMapper<Payment, PaymentDto> {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    protected String bankAccountToId(BankAccount bankAccount) {
        if(null == bankAccount) {
            return null;
        }

        return bankAccount.getId().toString();
    }

    protected BankAccount idToBankAccount(String bankAccountId) {
        if(Strings.isEmpty(bankAccountId)) {
            return null;
        }
        return bankAccountRepository.getReferenceById(UUID.fromString(bankAccountId));
    }

    protected LocalDate stringToLocalDate(String date) {
        return Strings.isBlank(date) ? null : LocalDate.parse(date);
    }
}
