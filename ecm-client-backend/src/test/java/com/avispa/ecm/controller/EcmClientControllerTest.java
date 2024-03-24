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
import com.avispa.ecm.model.ui.layout.dto.SectionDto;
import com.avispa.ecm.model.ui.layout.dto.WidgetDto;
import com.avispa.ecm.model.ui.menu.MenuService;
import com.avispa.ecm.model.ui.menu.dto.MenuDto;
import com.avispa.ecm.model.ui.menu.dto.MenuItemDto;
import com.avispa.ecm.util.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(value = EcmClientController.class, properties = "spring.main.allow-bean-definition-overriding=true")
class EcmClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicationService applicationService;
    @MockBean
    private LayoutService layoutService;
    @MockBean
    private MenuService menuService;

    @TestConfiguration
    public static class TestVersion {
        @Bean
        public Version ecmClientVersion() {
            return getVersion();
        }
    }

    @Test
    void givenClientRequest_whenExecute_thenReturn200AndContentMatches() throws Exception {
        var applicationDto = getApplicationDto();
        var layoutDto = getLayoutDto();
        var menuDto = getMenuDto();
        var versions = List.of(getVersion());

        when(applicationService.getConfiguration()).thenReturn(applicationDto);
        when(layoutService.getConfiguration()).thenReturn(layoutDto);
        when(menuService.getConfiguration()).thenReturn(menuDto);

        EcmClientController.ClientDto expected = EcmClientController.ClientDto.builder()
                .applicationDto(applicationDto)
                .layout(layoutDto)
                .header(EcmClientController.ClientDto.Header
                        .builder()
                        .menu(menuDto).build()
                ).versions(versions)
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/client"))
                .andExpect(status().is(200))
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
    }

    @Test
    void givenVersionRequest_whenExecute_thenReturn200AndContentMatches() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/client/versions"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].componentName", is("Test component")))
                .andExpect(jsonPath("$[0].number", is("1.0")));
    }

    private static Version getVersion() {
        return new Version("Test component", "1.0");
    }

    private static ApplicationDto getApplicationDto() {
        return new ApplicationDto("Full name", "Short name", "Description");
    }

    private static LayoutDto getLayoutDto() {
        return LayoutDto.builder()
                .section(SectionDto.builder()
                        .location(SectionDto.SectionLocation.SIDEBAR)
                        .widget(WidgetDto.builder()
                                .label("Repository")
                                .active(true)
                                .type(WidgetDto.WidgetType.REPOSITORY)
                                .build())
                        .build())
                .build();
    }

    private static MenuDto getMenuDto() {
        return MenuDto.builder()
                .item(MenuItemDto.builder()
                        .label("Label")
                        .action("Action")
                        .build())
                .build();
    }
}