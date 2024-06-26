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
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import com.avispa.ecm.util.GenericService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        ListWidget listWidget = new ListWidget();
        listWidget.setType(type);
        listWidget.setCaption("caption");
        listWidget.setProperties(List.of(ListWidgetProperty.builder()
                        .name("objectName")
                        .build(),
                ListWidgetProperty.builder()
                        .label("Custom unit price")
                        .name("unitPrice")
                        .build()));
        listWidget.setEmptyMessage("empty");
        listWidget.setItemsPerPage(10);

        DtoObject dtoObject = new DtoObject();
        dtoObject.setType(type);
        dtoObject.setDtoClass(TestDocumentDto.class);
        when(dtoService.getDtoObjectFromType(type)).thenReturn(dtoObject);

        TestDocumentDto dto = new TestDocumentDto();
        dto.setUnitPrice(BigDecimal.TEN);
        dto.setObjectName("Test doc");
        when(genericService.getService(type.getEntityClass()).findAll(1, 10)).thenReturn(List.of(dto));

        when(displayService.getDisplayValueFromAnnotation(TestDocumentDto.class, "objectName")).thenAnswer(invocation -> invocation.getArguments()[1]);
        when(listDataDtoMapper.convert(dto, List.of("objectName", "unitPrice"))).thenReturn(getListDataDto());

        var actual = listWidgetService.getAllDataFrom(listWidget, 1);

        assertEquals(getExpected(), actual);
    }

    private ListWidgetDto getExpected() {
        ListWidgetDto listWidgetDto = new ListWidgetDto();
        listWidgetDto.setDocument(true);
        listWidgetDto.setResource("test-document");
        listWidgetDto.setCaption("caption");
        listWidgetDto.setEmptyMessage("empty");
        listWidgetDto.setHeaders(List.of("objectName", "Custom unit price"));
        listWidgetDto.setPagesNum(1);

        listWidgetDto.setData(List.of(getListDataDto()));

        return listWidgetDto;
    }

    private static ListDataDto getListDataDto() {
        ListDataDto dataDto = new ListDataDto();
        dataDto.setValues(Map.of("objectName", "Test doc"));
        dataDto.setValues(Map.of("unitPrice", "10.00"));
        return dataDto;
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 2})
    void givenPageNumberNotInRange_whenGetData_thenThrowException(int pageNumber) {
        Type type = new Type();
        type.setObjectName("Document");
        type.setEntityClass(Document.class);

        ListWidget listWidget = new ListWidget();
        listWidget.setType(type);
        listWidget.setItemsPerPage(10);

        assertThrows(ValidationException.class, () -> listWidgetService.getAllDataFrom(listWidget, pageNumber));
    }

    @Test
    void givenPagesNumberNotInRange_whenGetData_thenThrowException() {
        Type type = new Type();
        type.setObjectName("Document");
        type.setEntityClass(Document.class);

        ListWidget listWidget = new ListWidget();
        listWidget.setType(type);
        listWidget.setItemsPerPage(-20);

        assertThrows(ValidationException.class, () -> listWidgetService.getAllDataFrom(listWidget, 1));
    }

    @Test
    void givenDocuments_whenGetData_thenCorrectNumberOfPagesComputed() {
        Type type = new Type();
        type.setObjectName("Document");
        type.setEntityClass(Document.class);

        ListWidget listWidget = new ListWidget();
        listWidget.setType(type);
        listWidget.setProperties(List.of(ListWidgetProperty.builder()
                .name("objectName")
                .build()));
        listWidget.setItemsPerPage(3);

        DtoObject dtoObject = new DtoObject();
        dtoObject.setType(type);
        dtoObject.setDtoClass(TestDocumentDto.class);
        when(dtoService.getDtoObjectFromType(type)).thenReturn(dtoObject);

        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Test doc");
        when(genericService.getService(type.getEntityClass()).findAll(1, listWidget.getItemsPerPage())).thenReturn(List.of(dto));
        when(genericService.getService(type.getEntityClass()).count()).thenReturn(10L);

        // there are 10 items, and 3 items per page
        // ceil(10/3)=ceil(3,3333...)=4
        var listWidgetDto = listWidgetService.getAllDataFrom(listWidget, 1);
        assertEquals(4, listWidgetDto.getPagesNum());
    }
}