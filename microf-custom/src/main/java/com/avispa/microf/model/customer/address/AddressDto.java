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

package com.avispa.microf.model.customer.address;

import com.avispa.ecm.model.base.dto.Dto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class AddressDto implements Dto {
    public static final String VM_STREET_NOT_EMPTY_NOR_BLANK = "Street cannot be empty or blank";
    public static final String VM_PLACE_NOT_EMPTY_NOR_BLANK = "Place cannot be empty or blank";
    public static final String VM_ZIP_CODE_PATTERN_NOT_MATCH = "Zip code does not match specified pattern";

    private UUID id;

    @NotBlank(message = VM_STREET_NOT_EMPTY_NOR_BLANK)
    private String street;

    @NotBlank(message = VM_PLACE_NOT_EMPTY_NOR_BLANK)
    private String place;

    @Pattern(regexp = "[0-9]{2}-[0-9]{3}", message = VM_ZIP_CODE_PATTERN_NOT_MATCH)
    private String zipCode;

    @Override
    public String toString() {
        return street +
                System.getProperty("line.separator") +
                zipCode + " " + place;
    }
}
