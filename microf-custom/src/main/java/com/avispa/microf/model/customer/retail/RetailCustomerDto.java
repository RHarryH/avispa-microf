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

import com.avispa.ecm.model.base.dto.MultiTypeDto;
import com.avispa.microf.model.customer.CustomerDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class RetailCustomerDto extends CustomerDto implements MultiTypeDto<RetailCustomerDetailDto> {
    @JsonUnwrapped
    @Valid
    private RetailCustomerDetailDto details;
}
