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
import com.avispa.ecm.model.load.dto.LayoutDto;
import com.avispa.ecm.model.ui.layout.Layout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LayoutMapper extends EcmConfigMapper<Layout, LayoutDto> {
    @Override
    @Mapping(source = "name", target = "objectName")
    Layout convertToEntity(LayoutDto dto);

    @Override
    @Mapping(source = "name", target = "objectName")
    void updateEntityFromDto(LayoutDto dto, @MappingTarget Layout entity);
}
