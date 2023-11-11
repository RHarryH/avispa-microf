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

package com.avispa.ecm.model.base.controller;

import com.avispa.ecm.model.base.dto.Dto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
interface EcmController<D extends Dto> {
    void add(D dto);
    void update(D dto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Deletes object of provided resource with specific id")
    @ApiResponse(responseCode = "200", description = "Object was deleted", content = @Content)
    @ApiResponse(responseCode = "404", description = "Object was not found", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    void delete(
            @PathVariable("id")
            @Parameter(description = "id of the resource/object to update")
            UUID id,
            String resourceName);
}
