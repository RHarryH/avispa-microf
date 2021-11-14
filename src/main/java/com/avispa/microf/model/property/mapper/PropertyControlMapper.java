package com.avispa.microf.model.property.mapper;

import com.avispa.ecm.model.configuration.propertypage.controls.PropertyControl;
import com.avispa.microf.model.property.PropertyControlDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        config = ControlMapper.class)
public interface PropertyControlMapper extends BaseControlMapper{
    @Mapping(target = "name", source = "control.name")
    PropertyControlDto toPropertyControlDto(PropertyControl control, @Context Object object);
}