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
import com.avispa.microf.model.customer.corporate.CorporateCustomerDetailDto;
import com.avispa.microf.model.customer.retail.RetailCustomerDetailDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CustomerCommonDto extends CustomerDto implements Dto {
    @JsonUnwrapped
    private RetailCustomerDetailDto retailDetails;

    @JsonUnwrapped
    private CorporateCustomerDetailDto corporateDetails;
}
