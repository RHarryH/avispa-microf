/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.ecm.model.ui.widget.list.mapper;

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import com.avispa.ecm.model.ui.widget.list.dto.ListDataDto;
import com.avispa.ecm.util.reflect.EcmPropertyUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
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
        listDataDto.setValues(properties.stream()
                        .collect(Collectors.toMap(Function.identity(), property -> {
                            Object value = EcmPropertyUtils.getProperty(dto, property);
                            return null != value ?
                                    dictionaryService.getValueFromDictionary(dto.getClass(), property, value.toString()) :
                                    "-";
                        }, (x, y) -> y, LinkedHashMap::new)));
    }
}
