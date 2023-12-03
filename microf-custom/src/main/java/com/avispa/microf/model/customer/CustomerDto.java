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

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.configuration.dictionary.annotation.Dictionary;
import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.customer.address.AddressDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public abstract class CustomerDto implements Dto {
    public static final String VM_PHONE_PATTERN_NOT_MATCH = "Phone does not match specified pattern";
    public static final String VM_EMAIL_INVALID = "Email address is invalid";
    public static final String VM_EMAIL_NO_LONGER = "The email cannot be longer than 150 characters";
    public static final String VM_ADDRESS_NOT_NULL = "Address cannot be null";

    private UUID id;

    @DisplayName("Customer Name")
    private String objectName;

    @Dictionary(name = "CustomerType")
    @DisplayName("Customer Type")
    private String type;

    @Pattern(regexp = EMPTY_STRING_REGEX + "|(\\+48 \\d{9})", message = VM_PHONE_PATTERN_NOT_MATCH)
    @DisplayName("Phone Number")
    private String phoneNumber;

    @Email(message = VM_EMAIL_INVALID)
    @Size(max = 150, message = VM_EMAIL_NO_LONGER)
    @DisplayName("Email")
    private String email;

    @NotNull(message = VM_ADDRESS_NOT_NULL)
    @DisplayName("Address")
    private AddressDto address;
}
