/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.ecm.controller;

import com.avispa.ecm.model.configuration.propertypage.content.control.dictionary.DynamicLoad;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.DictionaryControlLoader;
import com.avispa.ecm.util.TypeNameUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/dictionary")
@Tag(name = "Dictionary", description = "Endpoints for reloading of dynamic dictionaries")
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryControlLoader dictionaryControlLoader;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DictionaryDto {
        private String qualification;
        private Map<String, Object> object;
    }

    @PostMapping("/{resourceName}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns values of a dynamic dictionary")
    @ApiResponse(responseCode = "200", description = "Dictionary values have been returned", content = @Content)
    public Map<String, String> loadDictionary(@PathVariable String resourceName, @RequestBody DictionaryDto context) {
        var dynamicLoad = new DynamicLoad(TypeNameUtils.convertResourceNameToTypeName(resourceName), context.getQualification());

        return dictionaryControlLoader.loadDynamicDictionary(dynamicLoad, context.getObject());
    }
}
