package com.avispa.microf.model.invoice.payment;

import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.bankaccount.BankAccountRepository;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class PaymentMapper implements IEntityDtoMapper<Payment, PaymentDto> {

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
        return Hibernate.unproxy(bankAccountRepository.getReferenceById(UUID.fromString(bankAccountId)), BankAccount.class);
    }

    protected LocalDate stringToLocalDate(String date) {
        return Strings.isBlank(date) ? null : LocalDate.parse(date);
    }
}
