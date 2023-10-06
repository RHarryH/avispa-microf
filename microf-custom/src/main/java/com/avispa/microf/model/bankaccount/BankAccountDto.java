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

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.util.validation.account.IBANConstraint;
import com.avispa.microf.util.validation.account.NRBConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class BankAccountDto implements Dto {
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
    @JsonIgnore
    public String getAccountNumberWithoutCountry() {
        return accountNumber.substring(2);
    }
}
