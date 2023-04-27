package com.avispa.microf.model.ui.widget.list.mapper;

import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import com.avispa.ecm.util.reflect.PropertyUtils;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ListDataDtoMapper {
    @Autowired
    private DictionaryService dictionaryService;

    public abstract ListDataDto convert(Dto dto, @Context List<String> properties);

    @AfterMapping
    protected void getValues(Dto dto, @MappingTarget ListDataDto listDataDto, @Context List<String> properties) {
        Map<String, Object> map = PropertyUtils.introspect(dto);
        listDataDto.setValues(properties.stream()
                        .collect(Collectors.toMap(Function.identity(), property -> {
                            Object value = map.get(property);
                            return null != value ?
                                    dictionaryService.getValueFromDictionary(dto.getClass(), property, value.toString()) :
                                    "-";
                        }, (x, y) -> y, LinkedHashMap::new)));
    }
}
