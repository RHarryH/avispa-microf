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

package com.avispa.ecm.model.ui.modal;

import com.avispa.ecm.model.EcmObjectService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.ui.modal.page.ModalPageType;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.testdocument.TestDocument;
import com.avispa.ecm.testdocument.TestDocumentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ModalServiceTest {

    @Mock
    private PropertyPageService propertyPageService;

    @Mock
    private EcmObjectService ecmObjectService;

    @Mock
    private DtoService dtoService;

    @InjectMocks
    private ModalService modalService;

    @Test
    void givenResourceName_whenGetAddModal_thenReturnDto() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        DtoObject dtoObject = new DtoObject();
        dtoObject.setDtoClass(TestDocumentDto.class);
        dtoObject.setType(type);

        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(dtoService.getDtoObjectFromTypeName("Test document")).thenReturn(dtoObject);
        when(propertyPageService.getPropertyPage(eq(type.getEntityClass()), any(Dto.class))).thenReturn(propertyPageContent);

        ModalDto actualModalDto = modalService.getAddModal("test-document");
        ModalDto expectedModalDto = getExpectedAddModalDto(propertyPageContent);

        assertEquals(expectedModalDto, actualModalDto);
    }

    private static ModalDto getExpectedAddModalDto(PropertyPageContent propertyPageContent) {
        return ModalDto.builder()
                .title("Add new test document")
                .type(ModalType.ADD)
                .resource("test-document")
                .propertyPage(propertyPageContent)
                .action(Action.builder()
                        .endpoint("test-document")
                        .method(HttpMethod.POST)
                        .successMessage("Test document added successfully!")
                        .errorMessage("Test document adding failed!")
                        .buttonValue("Add")
                        .build())
                .pages(List.of(ModalPageDto.builder()
                        .name(ModalPageType.PROPERTIES.getName())
                        .pageType(ModalPageType.PROPERTIES)
                        .propertyPageConfig(propertyPageContent.getId())
                        .build()))
                .build();
    }

    @Test
    void givenResourceName_whenGetUpdateModal_thenReturnDto() {
        UUID randomUUID = UUID.randomUUID();

        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(ecmObjectService.getEcmObjectFrom(randomUUID, "Test document")).thenReturn(new TestDocument());
        when(dtoService.convertObjectToDto(any(TestDocument.class))).thenReturn(new TestDocumentDto());
        when(propertyPageService.getPropertyPage(eq(type.getEntityClass()), any(Dto.class))).thenReturn(propertyPageContent);

        ModalDto actualModalDto = modalService.getUpdateModal("test-document", randomUUID);
        ModalDto expectedModalDto = getExpectedUpdateModalDto(randomUUID, propertyPageContent);

        assertEquals(expectedModalDto, actualModalDto);
    }

    private static ModalDto getExpectedUpdateModalDto(UUID id, PropertyPageContent propertyPageContent) {
        return ModalDto.builder()
                .title("Update test document")
                .type(ModalType.UPDATE)
                .resource("test-document")
                .propertyPage(propertyPageContent)
                .action(Action.builder()
                        .endpoint("test-document/" + id)
                        .method(HttpMethod.POST)
                        .successMessage("Test document updated successfully!")
                        .errorMessage("Test document updating failed!")
                        .buttonValue("Update")
                        .build())
                .pages(List.of(ModalPageDto.builder()
                        .name(ModalPageType.PROPERTIES.getName())
                        .pageType(ModalPageType.PROPERTIES)
                        .propertyPageConfig(propertyPageContent.getId())
                        .build()))
                .build();
    }
}