package com.avispa.microf.model.invoice.payment;

import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class PaymentDto implements Dto {
    public static final String VM_PAYMENT_METHOD_NOT_EMPTY = "Payment type cannot be empty";
    public static final String VM_PAYMENT_DEADLINE_NOT_IN_RANGE = "Payment deadline must be in range 1 to 365 days";
    public static final String VM_PAID_AMOUNT_POSITIVE_OR_ZERO = "Paid amount must be greater or equal to 0";
    public static final String VM_PAID_AMOUNT_NOT_NULL = "Paid amount cannot be empty";
    public static final String VM_PAID_AMOUNT_IN_RANGE = "Paid amount out of range (expected <7 digits>.<2 digits>)";
    public static final String VM_DATE_NOT_EMPTY = "Payment date cannot be empty when paid amount is greater than zero";
    public static final String VM_DEADLINE_AND_BANK_ACCOUNT_NOT_PRESENT = "Deadline and bank account details should be provided for non-cash payment methods";

    private UUID id;

    @Dictionary(name = "PaymentMethod")
    @NotEmpty(message = VM_PAYMENT_METHOD_NOT_EMPTY)
    @DisplayName("Method")
    private String method = "BANK_TRANSFER";

    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Digits(integer=7, fraction=2, message = VM_PAID_AMOUNT_IN_RANGE)
    @PositiveOrZero(message = VM_PAID_AMOUNT_POSITIVE_OR_ZERO)
    @NotNull(message = VM_PAID_AMOUNT_NOT_NULL)
    @DisplayName("Paid amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @DisplayName("Paid amount date")
    private String paidAmountDate;

    @DisplayName("Deadline (days)")
    @Range(min = 1, max = 365, message = VM_PAYMENT_DEADLINE_NOT_IN_RANGE)
    private Integer deadline = 14;

    private String bankAccount;

    @AssertTrue(message = VM_DATE_NOT_EMPTY)
    public boolean isDateOfPaidNotEmptyWhenPaidAmountGreaterThanZero() {
        if(null != paidAmount && paidAmount.compareTo(BigDecimal.ZERO) > 0) { // greater than zero
            return Strings.isNotEmpty(paidAmountDate);
        }

        return true;
    }

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
