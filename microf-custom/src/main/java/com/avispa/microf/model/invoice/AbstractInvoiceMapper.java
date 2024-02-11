/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.base.mapper.EntityDtoMapper;
import com.avispa.microf.model.invoice.position.Position;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.position.PositionMapper;
import com.google.common.collect.MoreCollectors;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class AbstractInvoiceMapper<I extends BaseInvoice, D extends BaseInvoiceDto> implements EntityDtoMapper<I, D> {
    // not required when componentModel = "spring", can be autowired
    //InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Autowired
    protected PositionMapper positionMapper;

    @Mapping(target = "pdfRenditionAvailable", expression = "java(entity.isPdfRenditionAvailable())")
    public abstract D convertToDto(I entity);

    protected void updatePositionsFromPositionsDto(List<PositionDto> positionDtos, @MappingTarget List<Position> positions) {
        if ( positionDtos == null ) {
            return;
        }

        for (int i = 0; i < positionDtos.size(); i++) {
            PositionDto positionDto = positionDtos.get(i);
            Optional<Position> position = positions.stream()
                    .filter(p -> p.getId().equals(positionDto.getId()))
                    .collect(MoreCollectors.toOptional());

            if(position.isPresent()) {
                positionMapper.updateEntityFromDto(positionDto, position.get());
            } else {
                positions.add(i, positionMapper.convertToEntity(positionDto));
            }
        }
    }
}