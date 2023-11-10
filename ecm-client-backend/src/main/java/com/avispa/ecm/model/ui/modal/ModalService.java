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
import com.avispa.ecm.util.TypeNameUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
public class ModalService {
    private final ModalPageService modalPageService;

    public ModalDto getAddModal(String resourceName) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);

        return ModalDto.builder()
                .title("Add new " + typeName.toLowerCase(Locale.ROOT))
                .type(ModalType.ADD)
                .resource(resourceName)
                .propertyPage(modalPageService.loadPropertiesPage(typeName))
                .action(Action.builder()
                        .endpoint(resourceName)
                        .method(HttpMethod.POST)
                        .successMessage(typeName + " added successfully!")
                        .errorMessage(typeName + " adding failed!")
                        .buttonValue("Add")
                        .build())
                .pages(List.of(ModalPageDto.builder()
                        .name(ModalPageType.PROPERTIES.getName())
                        .pageType(ModalPageType.PROPERTIES)
                        .build()))
                .build();
    }

    public ModalDto getUpdateModal(String resourceName, UUID id) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);

        return ModalDto.builder()
                .title("Update " + typeName.toLowerCase(Locale.ROOT))
                .type(ModalType.UPDATE)
                .resource(resourceName)
                .propertyPage(modalPageService.loadPropertiesPage(typeName, id))
                .action(Action.builder()
                        .endpoint(resourceName + "/" + id)
                        .method(HttpMethod.POST)
                        .successMessage(typeName + " updated successfully!")
                        .errorMessage(typeName + " updating failed!")
                        .buttonValue("Update")
                        .build())
                .pages(List.of(ModalPageDto.builder()
                        .name(ModalPageType.PROPERTIES.getName())
                        .pageType(ModalPageType.PROPERTIES)
                        .build()))
                .build();
    }

    public ModalDto getCloneModal(String resourceName) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);

        return ModalDto.builder()
                .title("Clone existing " + typeName.toLowerCase(Locale.ROOT))
                .type(ModalType.CLONE)
                .resource(resourceName)
                .propertyPage(modalPageService.loadSourcePage(typeName))
                .action(Action.builder()
                        .endpoint(resourceName)
                        .method(HttpMethod.POST)
                        .successMessage(typeName + " cloned successfully!")
                        .errorMessage(typeName + " cloning failed!")
                        .buttonValue("Clone")
                        .build())
                .pages(List.of(
                        ModalPageDto.builder()
                                .name(ModalPageType.SELECT_SOURCE.getName())
                                .pageType(ModalPageType.SELECT_SOURCE)
                                .build(),
                        ModalPageDto.builder()
                                .name(ModalPageType.PROPERTIES.getName())
                                .pageType(ModalPageType.PROPERTIES)
                                .build()))
                .build();
    }

    public PropertyPageContent loadPage(@NonNull ModalPageEcmContext context, String resourceName) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);

        return modalPageService.loadPage(context, typeName);
    }
}