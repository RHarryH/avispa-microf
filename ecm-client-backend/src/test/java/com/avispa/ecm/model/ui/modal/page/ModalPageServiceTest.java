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

package com.avispa.ecm.model.ui.modal.page;

import com.avispa.ecm.model.EcmObjectService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContext;
import com.avispa.ecm.model.ui.modal.context.SourcePageContextInfo;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import com.avispa.ecm.util.exception.EcmException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
class ModalPageServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private PropertyPageService propertyPageService;
    private EcmObjectService ecmObjectService;
    private DtoService dtoService;
    private ModalPageService modalPageService;

    @BeforeEach
    void init() {
        propertyPageService = mock(PropertyPageService.class);
        ecmObjectService = mock(EcmObjectService.class);
        dtoService = mock(DtoService.class);

        modalPageService = new ModalPageService(propertyPageService, ecmObjectService, dtoService, objectMapper);
    }

    @Test
    void verifySourcePropertyPageWasLoaded() {
        modalPageService.loadSourcePage("Test document");

        verify(propertyPageService).getPropertyPage("Select source property page", SourcePageContextInfo.builder()
                .typeName("Test document")
                .build());
    }

    @Test
    void verifyPropertiesPropertyPageWasLoadedWhenIdNotProvided() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        DtoObject dtoObject = new DtoObject();
        dtoObject.setDtoClass(TestDocumentDto.class);
        dtoObject.setType(type);

        when(dtoService.getDtoObjectFromTypeName("Test document")).thenReturn(dtoObject);

        modalPageService.loadPropertiesPage("Test document");

        verify(propertyPageService).getPropertyPage(eq(TestDocument.class), any(Dto.class));
    }

    @Test
    void verifyPropertiesPropertyPageWasLoadedWhenIdProvided() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        UUID id = UUID.randomUUID();

        when(ecmObjectService.getEcmObjectFrom(id, "Test document")).thenReturn(new TestDocument());
        when(dtoService.convertObjectToDto(any(TestDocument.class))).thenReturn(new TestDocumentDto());

        modalPageService.loadPropertiesPage("Test document", id);

        verify(propertyPageService).getPropertyPage(eq(TestDocument.class), any(Dto.class));
    }

    @Test
    void givenContextWithSelectSourceAsNextPageAndWithoutContextInfo_whenLoadPage_thenReturnSelectSourcePage() {
        PropertyPageContent propertyPageContent = new PropertyPageContent();
        when(propertyPageService.getPropertyPage("Select source property page", SourcePageContextInfo.builder()
                .typeName("Test document")
                .build())).thenReturn(propertyPageContent);
        var context = ModalPageEcmContext.builder()
                .targetPageType(ModalPageType.SELECT_SOURCE)
                .build();

        var actualPropertyPageContent = modalPageService.loadPage(context, "Test document");

        assertEquals(actualPropertyPageContent, propertyPageContent);
    }

    @Test
    @SneakyThrows
    void givenContextWithSelectSourceAsNextPageAndWithContextInfo_whenLoadPage_thenReturnSelectSourcePage() {
        PropertyPageContent propertyPageContent = new PropertyPageContent();
        when(propertyPageService.getPropertyPage("Select source property page", SourcePageContextInfo.builder()
                .typeName("Test document")
                .build())).thenReturn(propertyPageContent);

        var context = ModalPageEcmContext.builder()
                .sourcePageType(ModalPageType.SELECT_SOURCE)
                .targetPageType(ModalPageType.SELECT_SOURCE)
                .contextInfo(objectMapper.readTree("{\"typeName\": \"Test document\"}"))
                .build();

        var actualPropertyPageContent = modalPageService.loadPage(context, "Test document");

        assertEquals(actualPropertyPageContent, propertyPageContent);
    }

    @Test
    void givenContextWithPropertiesAsNextPageAndWithoutContextInfo_whenLoadPage_thenReturnPropertiesPage() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        DtoObject dtoObject = new DtoObject();
        dtoObject.setDtoClass(TestDocumentDto.class);
        dtoObject.setType(type);

        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(dtoService.getDtoObjectFromTypeName("Test document")).thenReturn(dtoObject);
        when(propertyPageService.getPropertyPage(eq(type.getEntityClass()), any(Dto.class))).thenReturn(propertyPageContent);

        var context = ModalPageEcmContext.builder()
                .targetPageType(ModalPageType.PROPERTIES)
                .build();

        var actualPropertyPageContent = modalPageService.loadPage(context, "Test document");

        assertEquals(actualPropertyPageContent, propertyPageContent);
    }

    @Test
    @SneakyThrows
    void givenContextWithPropertiesAsNextPageAndWithContextInfoAndWithMissingSourcePageType_whenLoadPage_thenException() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        JsonNode contextInfo = objectMapper.readTree("{\"sourceId\": \"123\"}");
        var context = ModalPageEcmContext.builder()
                .targetPageType(ModalPageType.PROPERTIES)
                .contextInfo(contextInfo)
                .build();

        assertThrows(EcmException.class, () -> modalPageService.loadPage(context, "Test document"));
    }

    @Test
    @SneakyThrows
    void givenContextWithPropertiesAsNextPageAndWithContextInfo_whenLoadPage_thenReturnPropertiesPage() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        PropertyPageContent propertyPageContent = new PropertyPageContent();

        UUID id = UUID.randomUUID();

        JsonNode contextInfo = objectMapper.readTree("{\"sourceId\": \"" + id + "\"}");
        var context = ModalPageEcmContext.builder()
                .sourcePageType(ModalPageType.SELECT_SOURCE)
                .targetPageType(ModalPageType.PROPERTIES)
                .contextInfo(contextInfo)
                .build();

        when(ecmObjectService.getEcmObjectFrom(id, "Test document")).thenReturn(new TestDocument());
        when(dtoService.convertObjectToDto(any(TestDocument.class))).thenReturn(new TestDocumentDto());
        //when(dtoService.convert(contextInfo, "Test document")).thenReturn(new TestDocumentDto());
        when(propertyPageService.getPropertyPage(eq(type.getEntityClass()), any(Dto.class))).thenReturn(propertyPageContent);

        var actualPropertyPageContent = modalPageService.loadPage(context, "Test document");

        assertEquals(actualPropertyPageContent, propertyPageContent);
    }
}