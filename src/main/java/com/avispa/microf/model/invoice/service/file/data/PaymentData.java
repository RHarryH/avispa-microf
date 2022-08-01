package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.numeral.NumeralToStringConverter;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;

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

    private PaymentData(Payment payment, LocalDate issueDate, BigDecimal grossValue, Dictionary paymentTypeDict) {
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
