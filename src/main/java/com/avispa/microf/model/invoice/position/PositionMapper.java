package com.avispa.microf.model.invoice.position;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper {
    PositionDto convertToDto(Position position);

    Position convertToEntity(PositionDto dto);

    void updatePositionFromDto(PositionDto dto, @MappingTarget Position position);
}
