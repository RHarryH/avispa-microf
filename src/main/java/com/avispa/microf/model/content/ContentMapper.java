package com.avispa.microf.model.content;

import com.avispa.ecm.model.content.Content;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContentMapper {

    @Mapping(source = "objectName", target = "name")
    @Mapping(source = "fileStorePath", target = "path")
    ContentDto convertToDto(Content content);

    @Mapping(source = "name", target = "objectName")
    @Mapping(source = "path", target = "fileStorePath")
    Content convertToEntity(ContentDto dto);
}