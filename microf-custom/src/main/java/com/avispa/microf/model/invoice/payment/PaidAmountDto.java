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

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PaidAmountDto implements Dto {
    public static final String VM_PAID_AMOUNT_POSITIVE_OR_ZERO = "Paid amount must be greater or equal to 0";
    public static final String VM_PAID_AMOUNT_NOT_NULL = "Paid amount cannot be empty";
    public static final String VM_PAID_AMOUNT_IN_RANGE = "Paid amount out of range (expected <7 digits>.<2 digits>)";
    public static final String VM_DATE_NOT_EMPTY = "Payment date cannot be empty when paid amount is greater than zero";

    private UUID id;

    @Digits(integer = 7, fraction = 2, message = VM_PAID_AMOUNT_IN_RANGE)
    @PositiveOrZero(message = VM_PAID_AMOUNT_POSITIVE_OR_ZERO)
    @NotNull(message = VM_PAID_AMOUNT_NOT_NULL)
    @DisplayName("Paid amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @DisplayName("Paid amount date")
    private String paidAmountDate;

    @JsonIgnore
    @AssertTrue(message = VM_DATE_NOT_EMPTY)
    public boolean isDateOfPaidNotEmptyWhenPaidAmountGreaterThanZero() {
        if (null != paidAmount && paidAmount.compareTo(BigDecimal.ZERO) > 0) { // greater than zero
            return Strings.isNotEmpty(paidAmountDate);
        }

        return true;
    }
}
