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

import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.configuration.display.DisplayService;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.ui.widget.list.dto.ListDataDto;
import com.avispa.ecm.model.ui.widget.list.dto.ListWidgetDto;
import com.avispa.ecm.model.ui.widget.list.mapper.ListDataDtoMapper;
import com.avispa.ecm.util.GenericService;
import com.avispa.ecm.util.TestDocumentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ListWidgetServiceTest {
    @Mock
    private DtoService dtoService;

    @Mock
    private DisplayService displayService;

    @Mock
    private ListDataDtoMapper listDataDtoMapper;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private GenericService genericService;

    @InjectMocks
    private ListWidgetService listWidgetService;

    @Test
    void givenListWidget_whenGetAllData_thenDtoRetrieved() {
        Type type = new Type();
        type.setObjectName("Document");
        type.setEntityClass(Document.class);

        ListWidget listWidget = new ListWidget();
        listWidget.setType(type);
        listWidget.setCaption("caption");
        listWidget.setProperties(List.of("objectName"));
        listWidget.setEmptyMessage("empty");

        DtoObject dtoObject = new DtoObject();
        dtoObject.setType(type);
        dtoObject.setDtoClass(TestDocumentDto.class);
        when(dtoService.getDtoObjectFromType(type)).thenReturn(dtoObject);

        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Test doc");
        when(genericService.getService(type.getEntityClass()).findAll()).thenReturn(List.of(dto));

        when(displayService.getDisplayValueFromAnnotation(TestDocumentDto.class, "objectName")).thenAnswer(invocation -> invocation.getArguments()[1]);
        when(listDataDtoMapper.convert(dto, List.of("objectName"))).thenReturn(getListDataDto());

        var actual = listWidgetService.getAllDataFrom(listWidget);

        assertEquals(getExpected(), actual);
    }

    private ListWidgetDto getExpected() {
        ListWidgetDto listWidgetDto = new ListWidgetDto();
        listWidgetDto.setDocument(true);
        listWidgetDto.setTypeName("document");
        listWidgetDto.setCaption("caption");
        listWidgetDto.setEmptyMessage("empty");
        listWidgetDto.setHeaders(List.of("objectName"));

        listWidgetDto.setData(List.of(getListDataDto()));

        return listWidgetDto;
    }

    private static ListDataDto getListDataDto() {
        ListDataDto dataDto = new ListDataDto();
        dataDto.setValues(Map.of("objectName", "Test doc"));
        return dataDto;
    }
}