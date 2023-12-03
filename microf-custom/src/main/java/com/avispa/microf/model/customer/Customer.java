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

package com.avispa.microf.model.customer;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.TypeDiscriminator;
import com.avispa.ecm.util.FormatUtils;
import com.avispa.microf.model.customer.address.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
@TypeDiscriminator(name = "type")
public class Customer extends EcmObject {
    private String phoneNumber; // kept as string because there is no use case to process it as number

    @Column(length = 150)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    private String type;

    // corporate
    private String companyName;

    @Column(length = 13)
    private String vatIdentificationNumber; // in Poland Numer Identyfikacji Podatkowej (NIP)

    // retail
    private String firstName;
    private String lastName;

    public String format() {
        if(null == type) {
            throw new IllegalStateException("Customer type not specified");
        }

        if(type.equals("CORPORATE")) {
            return corporateFormat();
        } else if(type.equals("RETAIL")) {
            return retailFormat();
        } else {
            return "Unknown Customer type";
        }
    }

    private String corporateFormat() {
        return companyName +
                FormatUtils.getNewLine() +
                FormatUtils.getNewLine() +
                getAddress() +
                FormatUtils.getNewLine() +
                "NIP: " + vatIdentificationNumber;
    }

    private String retailFormat() {
        return firstName + " " + lastName +
                FormatUtils.getNewLine() +
                FormatUtils.getNewLine() +
                getAddress();
    }
}
