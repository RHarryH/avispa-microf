package com.avispa.microf.model.ui.widget.list.mapper;

import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import com.avispa.ecm.util.reflect.PropertyUtils;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ListDataDtoMapper {
    @Autowired
    private DictionaryService dictionaryService;

    @Mapping(target = "hasPdfRendition", expression = "java(commonDto.hasPdfRendition())")
    public abstract ListDataDto convert(CommonDto commonDto, @Context List<String> properties);

    @AfterMapping
    protected void getValues(CommonDto commonDto, @MappingTarget ListDataDto listDataDto, @Context List<String> properties) {
        Map<String, Object> map = PropertyUtils.introspect(commonDto);
        listDataDto.setValues(properties.stream()
                .map(property -> {
                    Object value = map.get(property);
                    return dictionaryService.getValueFromDictionary(commonDto.getClass(), property, value.toString());
                })
                .collect(Collectors.toList()));
    }
}
