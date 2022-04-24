package com.avispa.microf.model.bankaccount;

import com.avispa.ecm.model.EcmObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class BankAccount extends EcmObject {
    @Column(length = 50)
    private String bankName;

    @Column(length = 28)
    private String accountNumber;
}
