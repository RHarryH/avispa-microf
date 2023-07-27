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

package com.avispa.ecm.model.load.mapper;

import com.avispa.ecm.model.configuration.load.mapper.EcmConfigMapper;
import com.avispa.ecm.model.load.dto.MenuItemDto;
import com.avispa.ecm.model.ui.menu.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuItemMapper extends EcmConfigMapper<MenuItem, MenuItemDto> {
    @Override
    @Mapping(source = "name", target = "objectName")
    MenuItem convertToEntity(MenuItemDto dto);

    @Override
    @Mapping(source = "name", target = "objectName")
    void updateEntityFromDto(MenuItemDto dto, @MappingTarget MenuItem entity);
}
