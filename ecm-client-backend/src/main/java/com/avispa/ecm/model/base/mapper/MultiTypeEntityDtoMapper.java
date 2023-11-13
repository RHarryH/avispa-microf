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

package com.avispa.ecm.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.base.dto.SubtypeDetailDto;
import com.avispa.ecm.model.base.dto.MultiTypeDto;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author Rafał Hiszpański
 */
public interface MultiTypeEntityDtoMapper<T extends EcmObject, D extends MultiTypeDto<E>, E extends SubtypeDetailDto> extends EntityDtoMapper<T, D> {
    @Override
    @Mapping(source = "dto", target = "details")
    D convertToDto(T dto);

    E toDetailDto(T dto);

    default void updateEntityFromDto(D dto, @MappingTarget T entity) {
        if (dto == null) {
            return;
        }

        updateEntityFromDto(dto, dto.getDetails(), entity);
    }

    void updateEntityFromDto(D dto, E detailDto, @MappingTarget T entity);

    @Override
    default T convertToEntity(D dto) {
        if (dto == null) {
            return null;
        }

        return convertToEntity(dto, dto.getDetails());
    }

    T convertToEntity(D dto, E detailDto);
}