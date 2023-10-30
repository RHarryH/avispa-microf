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

import com.avispa.ecm.model.base.mapper.EntityDtoMapper;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BankAccountMapper extends EntityDtoMapper<BankAccount, BankAccountDto> {
    @Override
    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "prettyPrintAccountNumber")
    BankAccountDto convertToDto(BankAccount entity);

    @Named("prettyPrintAccountNumber")
    default String prettyPrintAccountNumber(String accountNumber) {
        if(Strings.isEmpty(accountNumber)) {
            return accountNumber;
        }

        final int SPLIT = 4;
        String[] splitByFour = accountNumber.split("(?<=\\G.{" + SPLIT + "})");
        List<String> splitByFourList = Arrays.asList(splitByFour);
        List<String> splitByFourListExceptCountryAndControl = splitByFourList.subList(1, splitByFourList.size());

        return accountNumber.substring(0, 2) // country code
                + " "
                + accountNumber.substring(2, 4) // control code
                + " "
                + String.join(" ", splitByFourListExceptCountryAndControl);
    }

    @Override
    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "compactAccountNumber")
    BankAccount convertToEntity(BankAccountDto dto);

    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "compactAccountNumber")
    void updateEntityFromDto(BankAccountDto dto, @MappingTarget BankAccount entity);

    @Named("compactAccountNumber")
    default String compactAccountNumber(String accountNumber) {
        return Strings.isEmpty(accountNumber) ? accountNumber : accountNumber.replace(" ", "");
    }
}