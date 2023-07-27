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

package com.avispa.ecm.model.load.dto;

import com.avispa.ecm.model.configuration.load.dto.EcmConfigDto;
import lombok.Data;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Data
public class MenuItemDto implements EcmConfigDto {
    private String label;
    private String action;
    private List<MenuItemDto> items;

    @Override
    public String getName() {
        return label;
    }
}
