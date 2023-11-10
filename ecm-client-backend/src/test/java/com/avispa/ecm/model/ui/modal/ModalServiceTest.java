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

import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContext;
import com.avispa.ecm.model.ui.modal.page.ModalPageDto;
import com.avispa.ecm.model.ui.modal.page.ModalPageService;
import com.avispa.ecm.model.ui.modal.page.ModalPageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ModalServiceTest {
    @Mock
    private ModalPageService modalPageService;

    @InjectMocks
    private ModalService modalService;

    @Test
    void givenResourceName_whenGetAddModal_thenReturnDto() {
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(modalPageService.loadPropertiesPage("Test document")).thenReturn(propertyPageContent);

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
                        .build()))
                .build();
    }

    @Test
    void givenResourceName_whenGetUpdateModal_thenReturnDto() {
        UUID randomUUID = UUID.randomUUID();
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(modalPageService.loadPropertiesPage("Test document", randomUUID)).thenReturn(propertyPageContent);

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
                        .build()))
                .build();
    }

    @Test
    void givenResourceName_whenGetCloneModal_thenReturnDto() {
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        when(modalPageService.loadSourcePage("Test document")).thenReturn(propertyPageContent);

        ModalDto actualModalDto = modalService.getCloneModal("test-document");
        ModalDto expectedModalDto = getExpectedCloneModalDto(propertyPageContent);

        assertEquals(expectedModalDto, actualModalDto);
    }

    private static ModalDto getExpectedCloneModalDto(PropertyPageContent propertyPageContent) {
        return ModalDto.builder()
                .title("Clone existing test document")
                .type(ModalType.CLONE)
                .resource("test-document")
                .propertyPage(propertyPageContent)
                .action(Action.builder()
                        .endpoint("test-document")
                        .method(HttpMethod.POST)
                        .successMessage("Test document cloned successfully!")
                        .errorMessage("Test document cloning failed!")
                        .buttonValue("Clone")
                        .build())
                .pages(List.of(ModalPageDto.builder()
                        .name(ModalPageType.SELECT_SOURCE.getName())
                        .pageType(ModalPageType.SELECT_SOURCE)
                        .build(),
                            ModalPageDto.builder()
                        .name(ModalPageType.PROPERTIES.getName())
                        .pageType(ModalPageType.PROPERTIES)
                        .build()))
                .build();
    }

    @Test
    void givenResourceName_whenGetPage_thenReturnPropertyPage() {
        PropertyPageContent propertyPageContent = new PropertyPageContent();

        var pageContext = ModalPageEcmContext.builder()
                .targetPageType(ModalPageType.PROPERTIES)
                .build();

        when(modalPageService.loadPage(pageContext, "Test document")).thenReturn(propertyPageContent);

        PropertyPageContent actualPropertyPageContent = modalService.loadPage(pageContext, "test-document");

        assertEquals(propertyPageContent, actualPropertyPageContent);
    }
}