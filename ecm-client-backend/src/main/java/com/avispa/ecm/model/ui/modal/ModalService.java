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

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.ui.modal.page.ModalPageType;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.util.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    private final PropertyPageService propertyPageService;
    private final EcmObjectService ecmObjectService;
    private final DtoService dtoService;

    public ModalDto getAddModal(String resourceName) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);

        // in these cases we're creating an empty instance of entity and dto so there is no need
        // to check the discriminator
        DtoObject dtoObject = dtoService.getDtoObjectFromTypeName(typeName);

        // usage of Dto enables usage of default values, without that we can get empty values/table rows but the property
        // page will display normally, generally speaking the need of Dto here has been significantly reduced
        // note: when switching to EcmObject there is a need to provide @Dictionary annotation to all combo fields
        Dto dto = BeanUtils.instantiateClass(dtoObject.getDtoClass());

        var propertyPageContent = propertyPageService.getPropertyPage(dtoObject.getType().getEntityClass(), dto);

        return ModalDto.builder()
                .title("Add new " + typeName.toLowerCase(Locale.ROOT))
                .type(ModalType.ADD)
                .resource(resourceName)
                .propertyPage(propertyPageContent)
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
                        .propertyPageConfig(propertyPageContent.getId())
                        .build()))
                .build();
    }

    public ModalDto getUpdateModal(String resourceName, UUID id) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);

        EcmObject entity = ecmObjectService.getEcmObjectFrom(id, typeName);
        // usage of Dto enables usage of default values, without that we can get empty values/table rows but the property
        // page will display normally, generally speaking the need of Dto here has been significantly reduced
        // note: when switching to EcmObject there is a need to provide @Dictionary annotation to all combo fields
        Dto dto = dtoService.convertObjectToDto(entity);

        var propertyPageContent = propertyPageService.getPropertyPage(entity.getClass(), dto);

        return ModalDto.builder()
                .title("Update " + typeName.toLowerCase(Locale.ROOT))
                .type(ModalType.UPDATE)
                .resource(resourceName)
                .propertyPage(propertyPageContent)
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
                        .propertyPageConfig(propertyPageContent.getId())
                        .build()))
                .build();
    }
}