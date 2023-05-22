package com.avispa.ecm.model.content;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContentMapper {

    @Mapping(source = "objectName", target = "name")
    @Mapping(source = "fileStorePath", target = "path")
    ContentDto convertToDto(Content content);

    @Mapping(source = "name", target = "objectName")
    @Mapping(source = "path", target = "fileStorePath")
    Content convertToEntity(ContentDto dto);
}