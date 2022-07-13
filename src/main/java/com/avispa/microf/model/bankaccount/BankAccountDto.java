package com.avispa.microf.model.bankaccount;

import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.util.validation.account.IBANConstraint;
import com.avispa.microf.util.validation.account.NRBConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class BankAccountDto implements CommonDto {
    public static final String VM_ACCOUNT_NAME_NO_LONGER = "The account name cannot be longer than 50 characters";
    public static final String VM_BANK_NAME_NO_LONGER = "The bank name cannot be longer than 50 characters";

    private UUID id;

    @Size(max = 50, message = VM_ACCOUNT_NAME_NO_LONGER)
    @DisplayName("Account Name")
    private String objectName;

    @Size(max = 50, message = VM_BANK_NAME_NO_LONGER)
    @DisplayName("Bank Name")
    private String bankName;

    @IBANConstraint
    @NRBConstraint
    @DisplayName("Account Number")
    private String accountNumber;

    /**
     * Returns IBAN number in NRB form (without two first letters representing country)
     * @return
     */
    public String getAccountNumberWithoutCountry() {
        return accountNumber.substring(2);
    }
}
