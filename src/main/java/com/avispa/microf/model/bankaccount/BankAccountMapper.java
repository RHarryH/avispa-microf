package com.avispa.microf.model.bankaccount;

import com.avispa.microf.model.base.IEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BankAccountMapper extends IEntityDtoMapper<BankAccount, BankAccountDto> {
    @Override
    @Mapping(source = "objectName", target = "accountName")
    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "prettyPrintAccountNumber")
    BankAccountDto convertToDto(BankAccount entity);

    @Named("prettyPrintAccountNumber")
    default String prettyPrintAccountNumber(String accountNumber) {
        if(StringUtils.isEmpty(accountNumber)) {
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
    @Mapping(source = "accountName", target = "objectName")
    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "compactAccountNumber")
    BankAccount convertToEntity(BankAccountDto dto);

    @Mapping(source = "accountName", target = "objectName")
    @Mapping(source = "accountNumber", target = "accountNumber", qualifiedByName = "compactAccountNumber")
    void updateEntityFromDto(BankAccountDto dto, @MappingTarget BankAccount entity);

    @Named("compactAccountNumber")
    default String compactAccountNumber(String accountNumber) {
        return StringUtils.isEmpty(accountNumber) ? accountNumber : accountNumber.replace(" ", "");
    }
}