package com.avispa.microf.dto.content;

import com.avispa.ecm.model.content.Content;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "objectName", target = "name")
    @Mapping(source = "fileStorePath", target = "path")
    ContentDto convertToDto(Content content);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "objectName")
    @Mapping(source = "path", target = "fileStorePath")
    Content convertToEntity(ContentDto dto);
}