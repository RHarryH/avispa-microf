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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequestMapping("v1/modal")
@Tag(name = "Modal", description = "Endpoints for getting data required to create and manipulate modals")
public interface ModalOperations {

    @GetMapping("/add/{resourceName}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gets details required to create modal for adding new objects of specific resource")
    @ApiResponse(responseCode = "200", description = "Modal data has been returned", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    ModalDto getAddModal(
            @PathVariable("resourceName")
            @Parameter(description = "name of the resource, which is in fact an encoded type name")
            String resourceName);

    @GetMapping("/update/{resourceName}/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gets details required to create modal for updating existing objects of specific resource")
    @ApiResponse(responseCode = "200", description = "Modal data has been returned", content = @Content)
    @ApiResponse(responseCode = "404", description = "Object does not exist in the system", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    ModalDto getUpdateModal(
            @PathVariable("resourceName")
            @Parameter(description = "name of the resource, which is in fact an encoded type name")
            String resourceName,
            @PathVariable("id")
            @Parameter(description = "id of the resource/object to update")
            UUID id);

    @GetMapping("/clone/{resourceName}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Gets details required to create modal for cloning existing objects of specific resource")
    @ApiResponse(responseCode = "200", description = "Modal data has been returned", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    ModalDto getCloneModal(
            @PathVariable("resourceName")
            @Parameter(description = "name of the resource, which is in fact an encoded type name")
            String resourceName);

    @PostMapping("/page/{resourceName}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns new page for multi-page modal")
    @ApiResponse(responseCode = "200", description = "Property page data has been returned", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    PropertyPageContent loadPage(@RequestBody ModalPageEcmContext context,
                                 @PathVariable("resourceName")
                                 @Parameter(description = "name of the resource, which is in fact an encoded type name")
                                 String resourceName);
}
