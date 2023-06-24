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
