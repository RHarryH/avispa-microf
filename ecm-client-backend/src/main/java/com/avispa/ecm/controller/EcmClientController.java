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

package com.avispa.ecm.controller;

import com.avispa.ecm.model.ui.application.ApplicationService;
import com.avispa.ecm.model.ui.application.dto.ApplicationDto;
import com.avispa.ecm.model.ui.layout.LayoutService;
import com.avispa.ecm.model.ui.layout.dto.LayoutDto;
import com.avispa.ecm.model.ui.menu.MenuService;
import com.avispa.ecm.model.ui.menu.dto.MenuDto;
import com.avispa.ecm.util.Version;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/client")
@Tag(name = "ECM Client", description = "Endpoints for getting general client info like application details, versions or layout definition")
@RequiredArgsConstructor
public class EcmClientController {
    private final List<Version> versions;

    private final ApplicationService applicationService;
    private final LayoutService layoutService;
    private final MenuService menuService;

    @Getter
    @Builder
    public static class ClientDto {
        @JsonUnwrapped
        private ApplicationDto applicationDto;
        private LayoutDto layout;
        private Header header;
        private List<Version> versions;

        @Getter
        @Builder
        static class Header {
            private MenuDto menu;
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all basic application data including it's name and description, layout definition, menu definitions and version details.")
    @ApiResponse(responseCode = "200", description = "All basic application data has been returned", content = @Content)
    public ClientDto getClientInfo() {
        return ClientDto.builder()
                .applicationDto(applicationService.getConfiguration())
                .layout(layoutService.getConfiguration())
                .header(ClientDto.Header.builder()
                        .menu(menuService.getConfiguration())
                        .build())
                .versions(versions)
                .build();
    }

    @GetMapping("/versions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get versions of all components participating to the final structure of the application. It will be typically ECM, ECM Client and customization versions.")
    @ApiResponse(responseCode = "200", description = "Version data has been properly returned", content = @Content)
    public List<Version> getVersions() {
        return versions;
    }
}
