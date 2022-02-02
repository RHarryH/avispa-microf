package com.avispa.microf.model.invoice.position;

import com.avispa.ecm.model.configuration.dictionary.DictionaryValueMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = DictionaryValueMapper.class)
public interface PositionMapper {
    PositionDto convertToDto(Position position);

    Position convertToEntity(PositionDto dto);

    void updatePositionFromDto(PositionDto dto, @MappingTarget Position position);
}
