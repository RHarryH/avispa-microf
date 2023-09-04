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

package com.avispa.ecm.model.ui.widget;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/widget")
@Tag(name = "Widgets", description = "Endpoints for getting data required by widgets to be properly displayed")
@RequiredArgsConstructor
@Slf4j
public class WidgetController {

    private final EcmObjectRepository<EcmObject> ecmObjectRepository;
    private final PropertyPageMapper propertyPageMapper;
    private final ContextService contextService;

    @GetMapping(value={"/properties-widget/{id}"})
    public PropertiesWidgetDto getPropertiesWidget(@PathVariable UUID id) {
        // convert to dto
        // return null otherwise
        return ecmObjectRepository.findById(id).flatMap(ecmObject ->
                        contextService.getConfiguration(ecmObject, PropertyPage.class)
                                .map(propertyPage -> propertyPageMapper.convertToContent(propertyPage, ecmObject, true)))
                .map(PropertiesWidgetDto::new)
                .orElse(new PropertiesWidgetDto());
    }

    @Getter
    @NoArgsConstructor
    public static class PropertiesWidgetDto {
        private boolean objectFound;
        private PropertyPageContent propertyPage;

        public PropertiesWidgetDto(PropertyPageContent propertyPage) {
            this.objectFound = true;
            this.propertyPage = propertyPage;
        }
    }
}
