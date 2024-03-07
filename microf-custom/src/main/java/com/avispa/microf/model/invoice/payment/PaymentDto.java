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

import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.validator.constraints.Range;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PaymentDto extends PaidAmountDto {
    public static final String VM_PAYMENT_METHOD_NOT_EMPTY = "Payment type cannot be empty";
    public static final String VM_PAYMENT_DEADLINE_NOT_IN_RANGE = "Payment deadline must be in range 1 to 365 days";
    public static final String VM_DEADLINE_AND_BANK_ACCOUNT_NOT_PRESENT = "Deadline and bank account details should be provided for non-cash payment methods";

    @Dictionary(name = "PaymentMethod")
    @NotEmpty(message = VM_PAYMENT_METHOD_NOT_EMPTY)
    @DisplayName("Method")
    private String method = "BANK_TRANSFER";

    @DisplayName("Deadline (days)")
    @Range(min = 1, max = 365, message = VM_PAYMENT_DEADLINE_NOT_IN_RANGE)
    private Integer deadline = 14;

    private String bankAccount;

    @JsonIgnore
    @AssertTrue(message = VM_DEADLINE_AND_BANK_ACCOUNT_NOT_PRESENT)
    public boolean isDeadlineAndBankAccountArePresentWhenNonCashMethod() {
        if(null == method) {
            return false;
        }

        if(!method.equals("CASH")) {
            return deadline != null && Strings.isNotEmpty(bankAccount);
        }

        return true;
    }
}
