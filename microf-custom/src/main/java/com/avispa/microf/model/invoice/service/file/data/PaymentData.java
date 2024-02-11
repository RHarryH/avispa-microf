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

package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.ecm.util.FormatUtils;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import com.avispa.microf.numeral.NumeralToStringConverter;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
@Getter
public class PaymentData {
    private final String status;
    private String method;

    private final BigDecimal paidAmount;
    private String paidAmountDate = "-";

    private String deadlineDate = "-";
    private final BigDecimal amount;
    private final String amountInWords;
    private String bankName = "-";
    private String bankAccountNumber = "-";

    public static PaymentData of(Invoice invoice, BigDecimal grossValue, Dictionary paymentTypeDict) {
        return new PaymentData(invoice.getPayment(), invoice.getIssueDate(), grossValue, paymentTypeDict);
    }

    private PaymentData(@NonNull Payment payment, LocalDate issueDate, BigDecimal grossValue, Dictionary paymentTypeDict) {
        this.paidAmount = payment.getPaidAmount();
        if(payment.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) { // only if paid amount is greater than zero
            this.paidAmountDate = FormatUtils.format(payment.getPaidAmountDate());
        }

        this.amount = getAmount(payment, grossValue);
        this.status = determineStatus();

        setMethod(payment, issueDate, paymentTypeDict);

        this.amountInWords = getAmountInWords(this.amount);
    }

    private BigDecimal getAmount(Payment payment, BigDecimal grossValue) {
        return grossValue.subtract(payment.getPaidAmount());
    }

    private String determineStatus() {
        int balanceCompare = this.amount.compareTo(BigDecimal.ZERO);
        if(balanceCompare < 0) { // negative number - excess payment
            return "NADPŁATA";
        } else {
            return "DO ZAPŁATY";
        }
    }

    private void setMethod(Payment payment, LocalDate issueDate, Dictionary paymentTypeDict) {
        String paymentMethodKey = payment.getMethod();
        this.method = paymentTypeDict.getLabel(paymentMethodKey);
        if(this.amount.compareTo(BigDecimal.ZERO) > 0) { // there is still amount to pay
            this.deadlineDate = getDeadlineDate(issueDate, payment.getDeadline());

            if (!paymentMethodKey.equals("CASH")) {
                BankAccount bankAccount = payment.getBankAccount();
                this.bankName = bankAccount.getBankName();
                this.bankAccountNumber = bankAccount.getFormattedAccountNumber();
            }
        }
    }

    private String getAmountInWords(BigDecimal grossValueSum) {
        return NumeralToStringConverter.convert(grossValueSum);
    }

    private String getDeadlineDate(LocalDate issueDate, Integer deadline) {
        return FormatUtils.format(issueDate.plusDays(deadline));
    }
}
