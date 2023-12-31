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

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.ui.modal.ModalType;
import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContext;
import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContextInfo;
import com.avispa.ecm.model.ui.modal.context.SourcePageContextInfo;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.util.exception.EcmException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModalPageService {
    private final PropertyPageService propertyPageService;
    private final EcmObjectService ecmObjectService;
    private final DtoService dtoService;

    private final ObjectMapper objectMapper;

    public PropertyPageContent loadPage(@NonNull ModalPageEcmContext context, String typeName) {
        ModalType modalType = context.getModalType();
        ModalPageType targetPageType = context.getTargetPageType();
        ModalPageEcmContextInfo contextInfo = extractContextInfo(context, typeName);

        return switch (targetPageType) {
            case SELECT_SOURCE -> loadSourcePage(typeName, contextInfo);
            case PROPERTIES -> loadPropertiesPage(typeName, modalType, contextInfo);
        };
    }

    private ModalPageEcmContextInfo extractContextInfo(ModalPageEcmContext context, String typeName) {
        if (context.getContextInfo() != null) {
            try {
                var contextInfoTree = context.getContextInfo();

                if (context.getSourcePageType() == ModalPageType.SELECT_SOURCE) {
                    return objectMapper.treeToValue(contextInfoTree, SourcePageContextInfo.class);
                } else if (context.getSourcePageType() == ModalPageType.PROPERTIES) {
                    return dtoService.convert(contextInfoTree, typeName);
                } else {
                    throw new EcmException("When context is provided it is also expected to provide source page type");
                }
            } catch (JsonProcessingException e) {
                log.error("Can't convert context info to context object: {}", context.getContextInfo().toPrettyString(), e);
                throw new EcmException("Can't convert context info to context object", e);
            }
        } else {
            log.warn("No context details are provided for modal page. Loading of target modal page will be performed with default settings");
        }

        return null;
    }

    public PropertyPageContent loadSourcePage(@NonNull String typeName) {
        return loadSourcePage(typeName, null);
    }

    private PropertyPageContent loadSourcePage(String typeName, ModalPageEcmContextInfo contextInfo) {
        SourcePageContextInfo sourcePageContext;
        if(contextInfo instanceof SourcePageContextInfo) {
            sourcePageContext = (SourcePageContextInfo) contextInfo;
        } else {
            sourcePageContext = SourcePageContextInfo.builder()
                    .typeName(typeName)
                    .build();
        }

        return propertyPageService.getPropertyPage("Select source property page", sourcePageContext, false);
    }

    public PropertyPageContent loadPropertiesPage(String typeName, ModalType modalType) {
        return loadPropertiesPage(typeName, modalType, (ModalPageEcmContextInfo) null);
    }

    public PropertyPageContent loadPropertiesPage(String typeName, ModalType modalType, UUID id) {
        return loadPropertiesPage(typeName, modalType, SourcePageContextInfo.builder()
                .sourceId(id)
                .build());
    }

    private PropertyPageContent loadPropertiesPage(String typeName, ModalType modalType, ModalPageEcmContextInfo contextInfo) {
        if (contextInfo instanceof SourcePageContextInfo sourcePageContext &&
                null != ((SourcePageContextInfo) contextInfo).getSourceId()) {

            EcmObject entity = ecmObjectService.getEcmObjectFrom(sourcePageContext.getSourceId(), typeName);
            // usage of Dto enables usage of default values, without that we can get empty values/table rows but the property
            // page will display normally, generally speaking the need of Dto here has been significantly reduced
            // note: when switching to EcmObject there is a need to provide @Dictionary annotation to all combo fields
            Dto dto = dtoService.convertObjectToDto(entity);

            return propertyPageService.getPropertyPage(entity.getClass(), dto, modalType == ModalType.UPDATE);
        } else {
            // in these cases we're creating an empty instance of entity and dto so there is no need
            // to check the discriminator
            DtoObject dtoObject = dtoService.getDtoObjectFromTypeName(typeName);

            // usage of Dto enables usage of default values, without that we can get empty values/table rows but the property
            // page will display normally, generally speaking the need of Dto here has been significantly reduced
            // note: when switching to EcmObject there is a need to provide @Dictionary annotation to all combo fields
            Dto dto = BeanUtils.instantiateClass(dtoObject.getDtoClass());

            return propertyPageService.getPropertyPage(dtoObject.getType().getEntityClass(), dto, modalType == ModalType.UPDATE);
        }
    }
}
