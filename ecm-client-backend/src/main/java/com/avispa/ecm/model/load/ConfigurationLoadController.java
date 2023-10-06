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

package com.avispa.ecm.model.load;

import com.avispa.ecm.model.configuration.load.ConfigurationLoadService;
import com.avispa.ecm.model.zip.ZipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/configuration")
@Tag(name = "Configuration Load", description = "Allows to manage zip configuration used for customization of Avispa ECM")
@RequiredArgsConstructor
public class ConfigurationLoadController {
    private final ConfigurationLoadService configurationLoadService;

    @PostMapping(value = "load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Loads zip configuration with customization")
    @ApiResponse(responseCode = "201", description = "Configuration file has been properly loaded into database", content = @Content)
    @ApiResponse(responseCode = "400", description = "Configuration file can't be loaded", content = @Content)
    public void load(@RequestParam("configurationFile") MultipartFile file,
                     @RequestParam(value = "override", defaultValue = "false") boolean override) {
        try {
            if(ZipService.isZipFile(file.getInputStream())) {
                configurationLoadService.load(file.getInputStream(), override);
            } else {
               throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "File is not a zip");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Can't load the configuration", e);
        }
    }
}
