package com.avispa.microf.model.bankaccount;

import com.avispa.microf.model.base.dto.IDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class BankAccountDto implements IDto {
    public static final String VM_ACCOUNT_NAME_NO_LONGER = "The account name cannot be longer than 50 characters";
    public static final String VM_BANK_NAME_NO_LONGER = "The bank name cannot be longer than 50 characters";

    private UUID id;

    @Size(max = 50, message = VM_ACCOUNT_NAME_NO_LONGER)
    private String accountName;

    @Size(max = 50, message = VM_BANK_NAME_NO_LONGER)
    private String bankName;

    private String accountNumber;

    /**
     * Returns IBAN number in NRB form (without two first letters representing country)
     * @return
     */
    public String getAccountNumberWithoutCountry() {
        return accountNumber.substring(2);
    }
}
