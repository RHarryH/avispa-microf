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
import com.avispa.ecm.util.reflect.EcmPropertyUtils;
import jakarta.validation.ValidationException;
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

    public ListWidgetDto getAllDataFrom(ListWidget listWidget, int pageNumber) {
        Type type = listWidget.getType();

        int pagesNumber = getPagesNumber(type.getEntityClass(), listWidget.getItemsPerPage());
        if (pageNumber < 1 || pageNumber > pagesNumber) {
            throw new ValidationException("Page number can't be greater than maximum number of pages. Provided: " + pageNumber + ", max: " + pagesNumber);
        }

        ListWidgetDto listWidgetDto = new ListWidgetDto();
        listWidgetDto.setDocument(Document.class.isAssignableFrom(type.getEntityClass()));
        listWidgetDto.setCaption(listWidget.getCaption());
        listWidgetDto.setResource(TypeNameUtils.convertTypeNameToResourceName(type.getObjectName()));
        listWidgetDto.setEmptyMessage(listWidget.getEmptyMessage());
        listWidgetDto.setPagesNum(pagesNumber);

        DtoObject dtoObject = dtoService.getDtoObjectFromType(type);
        List<String> filteredProperties = listWidget.getProperties().stream()
                .filter(property -> null != EcmPropertyUtils.getField(dtoObject.getDtoClass(), property)) // exclude fields not present in the object
                .toList();

        listWidgetDto.setHeaders(getHeader(dtoObject, filteredProperties));
        listWidgetDto.setData(getData(type, pageNumber, listWidget.getItemsPerPage(), filteredProperties));

        return listWidgetDto;
    }

    /**
     * Get number of pages
     *
     * @param typeClass    type for which the count of items will be made
     * @param itemsPerPage how many items per page to display
     * @return
     */
    private int getPagesNumber(Class<? extends EcmObject> typeClass, int itemsPerPage) {
        if (itemsPerPage < 0) {
            throw new ValidationException("Items per page must be greater than 0");
        }

        long count = count(typeClass);
        log.debug("Found {} records, items per page: {}", count, itemsPerPage);
        return Math.max((int) Math.ceil(count / (double) itemsPerPage), 1);
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
    private List<ListDataDto> getData(Type type, int pageNumber, int itemsPerPage, List<String> properties) {
        List<Dto> dtoList = findAll(type.getEntityClass(), pageNumber, itemsPerPage);

        return dtoList.stream()
                .map(dto -> listDataDtoMapper.convert(dto, properties))
                .toList();
    }

    private long count(Class<? extends EcmObject> entityClass) {
        return genericService.getService(entityClass).count();
    }

    private List<Dto> findAll(Class<? extends EcmObject> entityClass, int pageNumber, int itemsPerPages) {
        return genericService.getService(entityClass).findAll(pageNumber, itemsPerPages);
    }
}
