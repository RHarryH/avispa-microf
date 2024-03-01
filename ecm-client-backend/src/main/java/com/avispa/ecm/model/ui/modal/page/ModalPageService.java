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
import com.avispa.ecm.model.ui.modal.context.LinkDocumentContextInfo;
import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContext;
import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContextInfo;
import com.avispa.ecm.model.ui.modal.context.SourcePageContextInfo;
import com.avispa.ecm.model.ui.modal.link.LinkDocumentService;
import com.avispa.ecm.model.ui.modal.link.dto.LinkDocumentDto;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.util.exception.EcmException;
import com.avispa.ecm.util.reflect.EcmPropertyUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.avispa.ecm.model.ui.modal.ModalType.ADD;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModalPageService {
    private final PropertyPageService propertyPageService;
    private final EcmObjectService ecmObjectService;
    private final LinkDocumentService linkDocumentService;
    private final DtoService dtoService;

    private final ObjectMapper objectMapper;

    public PropertyPageContent loadPage(@NonNull ModalPageEcmContext context, String typeName) {
        ModalType modalType = context.getModalType();
        ModalPageType targetPageType = context.getTargetPageType();
        ModalPageEcmContextInfo contextInfo = extractContextInfo(context, typeName);

        return switch (targetPageType) {
            case SELECT_SOURCE -> loadSourcePage(typeName, contextInfo);
            case LINK_DOCUMENT -> loadLinkDocumentPage(typeName, contextInfo);
            case PROPERTIES -> loadPropertiesPage(typeName, modalType, contextInfo);
        };
    }

    /**
     * Extract context info from the modal context. Please note:
     * - When loading next page then both source and target pages type should be provided and the context info is
     * the context of current page.
     * - When loading previous page the context of the current page is not needed and therefore the source page type is
     * not required too. The context info must contain the target page type and the context info used to restore the
     * context of the previous page.
     *
     * @param context
     * @param typeName
     * @return
     */
    private ModalPageEcmContextInfo extractContextInfo(ModalPageEcmContext context, String typeName) {
        if (context.getContextInfo() != null) {
            try {
                var contextInfoTree = context.getContextInfo();

                // if source page is not provided it means the user is moving to the previous page
                ModalPageType pageType = context.getSourcePageType() != null ? context.getSourcePageType() :
                        context.getTargetPageType();

                if (pageType == ModalPageType.SELECT_SOURCE) {
                    return objectMapper.treeToValue(contextInfoTree, SourcePageContextInfo.class);
                } else if (pageType == ModalPageType.LINK_DOCUMENT) {
                    return objectMapper.treeToValue(contextInfoTree, LinkDocumentContextInfo.class);
                } else if (pageType == ModalPageType.PROPERTIES) {
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
            sourcePageContext.setTypeName(typeName); // enrich with type name (required for page restore)
        } else {
            sourcePageContext = SourcePageContextInfo.builder()
                    .typeName(typeName)
                    .build();
        }

        return propertyPageService.getPropertyPage("Select source property page", sourcePageContext, false);
    }

    /**
     * Load property page for linking documents
     *
     * @param typeName target type name (type which object will be created during the process of creation)
     * @param linkDocument link document configuration
     * @return
     */
    public PropertyPageContent loadLinkDocumentPage(@NonNull String typeName, LinkDocumentDto linkDocument) {
        return loadLinkDocumentPage(typeName, linkDocument, null);
    }

    /**
     * Load property page for linking documents
     *
     * @param typeName target type name (type which object will be created during the process of creation)
     * @param contextInfo context
     * @return
     */
    public PropertyPageContent loadLinkDocumentPage(@NonNull String typeName, ModalPageEcmContextInfo contextInfo) {
        return loadLinkDocumentPage(typeName, linkDocumentService.get(typeName), contextInfo);
    }

    private PropertyPageContent loadLinkDocumentPage(String typeName, LinkDocumentDto linkDocument, ModalPageEcmContextInfo contextInfo) {
        LinkDocumentContextInfo linkDocumentContext;
        if (contextInfo instanceof LinkDocumentContextInfo) {
            linkDocumentContext = (LinkDocumentContextInfo) contextInfo;
            linkDocumentContext.setTypeName(typeName); // enrich with type name (required for page restore)
            linkDocumentContext.setLinkDocument(linkDocument);
        } else {
            linkDocumentContext = LinkDocumentContextInfo.builder()
                    .typeName(typeName)
                    .linkDocument(linkDocument)
                    .build();
        }

        return propertyPageService.getPropertyPage("Link document property page", linkDocumentContext, false);
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

            var propertyPage = propertyPageService.getPropertyPage(entity.getClass(), dto, modalType == ModalType.UPDATE);

            // add id of linked object if any
            addLinkedDocument(typeName, modalType, entity, propertyPage);

            return propertyPage;
        } else {
            // in these cases we're creating an empty instance of entity and dto so there is no need
            // to check the discriminator
            DtoObject dtoObject = dtoService.getDtoObjectFromTypeName(typeName);

            if (dtoObject == null) {
                throw new EcmException("Dto object for '" + typeName + "' type was not found");
            }

            // usage of Dto enables usage of default values, without that we can get empty values/table rows but the property
            // page will display normally, generally speaking the need of Dto here has been significantly reduced
            // note: when switching to EcmObject there is a need to provide @Dictionary annotation to all combo fields
            Dto dto = BeanUtils.instantiateClass(dtoObject.getDtoClass());

            if (modalType == ADD && contextInfo instanceof LinkDocumentContextInfo linkDocumentContext) {
                var linkedId = linkDocument(dto, typeName, linkDocumentContext);

                var propertyPage = propertyPageService.getPropertyPage(dtoObject.getType().getEntityClass(), dto, false);

                // add control storing id of linked document, so it can be retrieved after submit of the page
                propertyPage.addHiddenControl(linkDocumentContext.getLinkDocument().getLinkProperty() + ".id", linkedId);

                return propertyPage;
            }

            return propertyPageService.getPropertyPage(dtoObject.getType().getEntityClass(), dto, modalType == ModalType.UPDATE);
        }
    }

    /**
     * Add linked document id if such link exists. This method should be used only for non-ADD mode when the linked
     * document already exists.
     *
     * @param typeName     name of the type for whch link configuration should be found
     * @param modalType    modal type
     * @param entity       object containing linked document
     * @param propertyPage property page to which hidden control should be added
     */
    private void addLinkedDocument(String typeName, ModalType modalType, EcmObject entity, PropertyPageContent propertyPage) {
        if (modalType != ADD) { // we should not be in ADD type but check it anyway
            LinkDocumentDto linkDocumentDto = linkDocumentService.find(typeName);
            if (linkDocumentDto != null) {
                var property = EcmPropertyUtils.getProperty(entity, linkDocumentDto.getLinkProperty());
                if (property instanceof EcmObject ecmObject) {
                    // add control storing id of linked document, so it can be retrieved after submit of the page
                    propertyPage.addHiddenControl(linkDocumentDto.getLinkProperty() + ".id", ecmObject.getId());
                }
            }
        }
    }

    /**
     * Link existing document with the currently created document. This method finds link document configuration
     * for the type of created document. Then it finds linked document in the repository, converts it to dto
     * and applies to the property pointed by the link document configuration.
     * <p>
     * NOTE: the name of the property in the configuration requires to have the same name of the property in both
     * entity and dto
     *
     * @param dto
     * @param typeName
     * @param linkDocumentContext
     * @return id of linked document
     */
    private UUID linkDocument(Dto dto, String typeName, LinkDocumentContextInfo linkDocumentContext) {
        LinkDocumentDto linkDocumentDto = linkDocumentService.get(typeName);

        linkDocumentContext.setLinkDocument(linkDocumentDto);

        EcmObject linkedEntity = ecmObjectService.getEcmObjectFrom(linkDocumentContext.getSourceId(), linkDocumentDto.getType());
        // usage of Dto enables usage of default values, without that we can get empty values/table rows but the property
        // page will display normally, generally speaking the need of Dto here has been significantly reduced
        // note: when switching to EcmObject there is a need to provide @Dictionary annotation to all combo fields
        Dto linkedDto = dtoService.convertObjectToDto(linkedEntity);

        EcmPropertyUtils.setProperty(dto, linkDocumentDto.getLinkProperty(), linkedDto);

        dto.inherit(); // run basic inheritance

        return linkedEntity.getId();
    }
}
