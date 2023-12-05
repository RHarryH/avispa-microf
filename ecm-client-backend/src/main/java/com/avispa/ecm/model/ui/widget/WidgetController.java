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
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.model.ui.widget.list.ListWidgetRepository;
import com.avispa.ecm.model.ui.widget.list.ListWidgetService;
import com.avispa.ecm.model.ui.widget.list.dto.ListWidgetDto;
import com.avispa.ecm.util.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private final ListWidgetRepository listWidgetRepository;
    private final PropertyPageService propertyPageService;

    private final ListWidgetService listWidgetService;

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

    @GetMapping(value={"/properties-widget/{id}"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns property page data with all values filled in.")
    @ApiResponse(responseCode = "200", description = "Property page data has been returned", content = @Content)
    @ApiResponse(responseCode = "404", description = "Property page configuration does not exist", content = @Content)
    public PropertiesWidgetDto getPropertiesWidget(
            @PathVariable
            @Parameter(description = "id of the object for which the property page should be returned")
            UUID id) {
        // convert to dto
        // return empty otherwise
        return ecmObjectRepository.findById(id).map(propertyPageService::getReadonlyPropertyPage)
                .map(PropertiesWidgetDto::new)
                .orElse(new PropertiesWidgetDto());
    }

    @GetMapping("/list-widget/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns list data containing all the data required to render list widget.")
    @ApiResponse(responseCode = "200", description = "List data has been returned", content = @Content)
    @ApiResponse(responseCode = "404", description = "Widget configuration does not exist", content = @Content)
    public ListWidgetDto getListWidget(
            @PathVariable
            @Parameter(description = "id of the list widget configuration containing list details (like type name and its property names to be displayed)")
            UUID id) {
        var listWidget = listWidgetRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return listWidgetService.getAllDataFrom(listWidget);
    }
}
