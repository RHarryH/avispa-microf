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

package com.avispa.ecm.model.ui.widget.list;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.configuration.display.DisplayService;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.ui.widget.list.dto.ListDataDto;
import com.avispa.ecm.model.ui.widget.list.dto.ListWidgetDto;
import com.avispa.ecm.model.ui.widget.list.mapper.ListDataDtoMapper;
import com.avispa.ecm.util.GenericService;
import com.avispa.ecm.util.TypeNameUtils;
import com.avispa.ecm.util.reflect.PropertyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ListWidgetService {
    private final DtoService dtoService;
    private final DisplayService displayService;

    private final ListDataDtoMapper listDataDtoMapper;

    private final GenericService genericService;

    public ListWidgetDto getAllDataFrom(ListWidget listWidget) {
        Type type = listWidget.getType();

        ListWidgetDto listWidgetDto = new ListWidgetDto();
        listWidgetDto.setDocument(Document.class.isAssignableFrom(type.getEntityClass()));
        listWidgetDto.setCaption(listWidget.getCaption());
        listWidgetDto.setResource(TypeNameUtils.convertTypeNameToResourceName(type.getObjectName()));
        listWidgetDto.setEmptyMessage(listWidget.getEmptyMessage());

        DtoObject dtoObject = dtoService.getDtoObjectFromType(type);
        List<String> filteredProperties = listWidget.getProperties().stream()
                .filter(property -> PropertyUtils.hasField(dtoObject.getDtoClass(), property)) // exclude fields not present in the object
                .toList();

        listWidgetDto.setHeaders(getHeader(dtoObject, filteredProperties));
        listWidgetDto.setData(getData(type, filteredProperties));

        return listWidgetDto;
    }

    /**
     * Header values stored in the database are just property names. They are then mapped by searching for
     * DisplayName annotation on the properties defined in Dto
     * @param dtoObject Dto type where searched DisplayName is defined
     * @param properties list of properties which should be visible on the list widget
     * @return
     */
    private List<String> getHeader(DtoObject dtoObject, List<String> properties) {

        return properties.stream()
                .map(property -> displayService.getDisplayValueFromAnnotation(dtoObject.getDtoClass(), property))
                .toList();
    }

    /**
     * Finds all data belonging to provided type and converts it to list format
     * @param type
     * @param properties
     * @return
     */
    private List<ListDataDto> getData(Type type, List<String> properties) {
        List<Dto> dtoList = findAll(type.getEntityClass());

        return dtoList.stream()
                .map(dto -> listDataDtoMapper.convert(dto, properties))
                .toList();
    }

    private List<Dto> findAll(Class<? extends EcmObject> entityClass) {
        return genericService.getService(entityClass).findAll();
    }
}
