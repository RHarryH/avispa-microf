package com.avispa.microf.model.invoice.payment;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.util.FormatUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class Payment extends EcmObject {

    @Column(nullable = false)
    @Dictionary(name = "PaymentMethod")
    private String method;

    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = FormatUtils.MONEY_DECIMAL_FORMAT)
    @Column(precision=9, scale=2, nullable = false)
    private BigDecimal paidAmount;

    @Column(columnDefinition = "DATE")
    private LocalDate paidAmountDate;

    private Integer deadline;

    @ManyToOne
    private BankAccount bankAccount;
}
