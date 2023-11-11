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

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
interface EcmModalableController {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Inserts new object")
    @ApiResponse(responseCode = "200", description = "Object was inserted", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    void add(@RequestBody JsonNode payload, String resourceName);

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates object with specific id")
    @ApiResponse(responseCode = "200", description = "Object was updated", content = @Content)
    @ApiResponse(responseCode = "404", description = "Object was not found", content = @Content)
    @ApiResponse(responseCode = "500", description = "Missing configuration data or unspecified error", content = @Content)
    void update(@RequestBody JsonNode payload, String resourceName, @PathVariable("id") UUID id);
}
