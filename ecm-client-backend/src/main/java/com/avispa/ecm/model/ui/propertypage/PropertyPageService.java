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

package com.avispa.ecm.model.ui.propertypage;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapperConfigurer;
import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.util.exception.RepositoryCorruptionError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class PropertyPageService {
    private static final String MISSING_PROPERTY_PAGE_ERROR = "Property page content can't be retrieved";

    private final ContextService contextService;
    private final PropertyPageMapper propertyPageMapper;
    private final EcmConfigRepository<PropertyPage> propertyPageRepository;

    /**
     * Gets the content of property page by finding upsert configuration matching provided ECM entity type
     * and by filling the labels, combo boxes, radios and other components (if any) with data from the context DTO.
     * Returned page is always available to edit.
     * @param entityClass ECM object which upsert configuration will be used
     * @param contextDto DTO object used as context for property page
     * @param edit true if page will be used for editing existing object
     * @return property page content with values
     */
    public PropertyPageContent getPropertyPage(Class<? extends EcmObject> entityClass, Dto contextDto, boolean edit) {
        return contextService.getConfiguration(entityClass, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.convertToContent(
                        getPropertyPageMode(edit), propertyPage, contextDto))
                .orElseThrow(() -> new RepositoryCorruptionError(MISSING_PROPERTY_PAGE_ERROR));
    }

    /**
     * Get property page from name only (like select source)
     * @param name property page name
     * @param context context object used to fill property page with values
     * @param edit true if page will be used for editing existing object
     * @return
     */
    public PropertyPageContent getPropertyPage(String name, Object context, boolean edit) {
        return propertyPageRepository.findByObjectName(name)
                .map(propertyPage -> propertyPageMapper.convertToContent(getPropertyPageMode(edit), propertyPage, context))
                .orElseThrow(() -> new RepositoryCorruptionError(MISSING_PROPERTY_PAGE_ERROR));
    }

    /**
     * Get property page mode based on simple boolean information
     *
     * @param edit
     * @return
     */
    private static PropertyPageMapperConfigurer getPropertyPageMode(boolean edit) {
        return edit ?
                PropertyPageMapperConfigurer.edit() :
                PropertyPageMapperConfigurer.insert();
    }

    /**
     * Get readonly property page by matching it with provided object type. Same object is later used to extract the
     * values. Useful for read operations when conversion to DTO is not required.
     * @param ecmObject object, which type will be used to find matching property page in the context
     * @return property page content with values
     */
    public PropertyPageContent getReadonlyPropertyPage(EcmObject ecmObject) {
        return contextService.getConfiguration(ecmObject, PropertyPage.class)
                .map(propertyPage ->
                        propertyPageMapper.convertToContent(
                                PropertyPageMapperConfigurer.readonly(),
                                propertyPage,
                                ecmObject))
                .orElseThrow(() -> new RepositoryCorruptionError(MISSING_PROPERTY_PAGE_ERROR));
    }
}
