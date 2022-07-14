package com.avispa.microf.model.ui.widget.list.mapper;

import com.avispa.ecm.util.reflect.PropertyUtils;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ListDataDtoMapper {
    @Mapping(target = "hasPdfRendition", expression = "java(commonDto.hasPdfRendition())")
    ListDataDto convert(CommonDto commonDto, @Context List<String> properties);

    @AfterMapping
    default void getValues(CommonDto commonDto, @MappingTarget ListDataDto listDataDto, @Context List<String> properties) {
        Map<String, Object> map = PropertyUtils.introspect(commonDto);
        listDataDto.setValues(properties.stream()
                .map(map::get)
                .map(Objects::toString)
                .collect(Collectors.toList()));
    }
}
