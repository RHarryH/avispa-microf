package com.avispa.microf.model.bankaccount;

import com.avispa.ecm.model.EcmObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public String getFormattedAccountNumber() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < accountNumber.length(); i+=4) {
            sb.append(accountNumber, i, Math.min(i + 4, accountNumber.length()));
            sb.append(" ");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
