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

package com.avispa.microf.model.customer.retail;

import com.avispa.microf.model.customer.CustomerDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class RetailCustomerDto extends CustomerDto {
    public static final String VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK = "First name cannot be empty or blank";
    public static final String VM_LAST_NAME_NOT_EMPTY_NOR_BLANK = "Last name cannot be empty or blank";

    @NotBlank(message = VM_FIRST_NAME_NOT_EMPTY_NOR_BLANK)
    private String firstName;

    @NotBlank(message = VM_LAST_NAME_NOT_EMPTY_NOR_BLANK)
    private String lastName;
}