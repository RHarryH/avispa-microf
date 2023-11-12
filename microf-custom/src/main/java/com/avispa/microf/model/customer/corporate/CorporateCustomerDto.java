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

package com.avispa.microf.model.customer.corporate;

import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.util.validation.VATINConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CorporateCustomerDto extends CustomerDto {
    public static final String VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK = "Company name cannot be empty or blank";
    public static final String VM_VIN_NOT_EMPTY = "VAT Identification Number cannot be empty";
    public static final String VM_VIN_PATTERN_NOT_MATCH = "VAT Identification Number does not match specified pattern";

    @NotBlank(message = VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK)
    private String companyName;

    @VATINConstraint
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{2}-\\d{3}", message = VM_VIN_PATTERN_NOT_MATCH)
    @NotEmpty(message = VM_COMPANY_NAME_NOT_EMPTY_NOR_BLANK)
    private String vatIdentificationNumber;
}
