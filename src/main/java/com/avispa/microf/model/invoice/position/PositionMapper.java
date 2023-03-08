package com.avispa.microf.model.invoice.position;

import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper extends IEntityDtoMapper<Position, PositionDto> {
}
