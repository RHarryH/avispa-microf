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

package com.avispa.ecm.model.ui.layout;

import com.avispa.ecm.EcmConfiguration;
import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.format.Format;
import com.avispa.ecm.model.ui.layout.dto.LayoutDto;
import com.avispa.ecm.model.ui.layout.dto.SectionDto;
import com.avispa.ecm.model.ui.layout.dto.WidgetDto;
import com.avispa.ecm.model.ui.layout.mapper.LayoutDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
@Slf4j
class LayoutServiceTest {
    @Mock
    private EcmConfigRepository<Layout> layoutRepository;

    private LayoutService layoutService;

    @BeforeEach
    void init() {
        LayoutDtoMapper layoutDtoMapper = new LayoutDtoMapper(new EcmConfiguration().jackson2ObjectMapperBuilder().build());
        layoutService = new LayoutService(layoutRepository, layoutDtoMapper);
    }

    @Test
    void givenLayoutInRepo_whenGet_thenReturned() {
        Layout layout = getLayout();

        ReflectionTestUtils.setField(layoutService, "layoutConfigName", "Layout");

        when(layoutRepository.findByObjectName("Layout")).thenReturn(Optional.of(layout));

        LayoutDto actualDto = layoutService.getConfiguration();
        LayoutDto expectedDto = getExpectedLayoutDto();

        assertEquals(expectedDto, actualDto);
    }

    private Layout getLayout() {
        Layout layout = new Layout();
        layout.setObjectName("Layout");

        layout.setContents(Set.of(createContent("configuration/content/layout-content.json")));

        return layout;
    }

    private Content createContent(String contentPath) {
        Content content = new Content();
        content.setFileStorePath(getPath(contentPath));
        Format format = new Format();
        format.setObjectName("application/json");
        content.setFormat(format);
        return content;
    }

    private String getPath(String resource) {
        try {
            return new ClassPathResource(resource).getFile().getAbsolutePath();
        } catch (IOException e) {
            log.error("Resource {} does not exist", resource);
        }

        return "";
    }

    private static LayoutDto getExpectedLayoutDto() {
        return LayoutDto.builder()
                .section(SectionDto.builder()
                        .location(SectionDto.SectionLocation.SIDEBAR)
                        .widget(WidgetDto.builder()
                                .label("Repository")
                                .active(true)
                                .type(WidgetDto.WidgetType.REPOSITORY)
                                .build())
                        .widget(WidgetDto.builder()
                                .label("Bank Account")
                                .type(WidgetDto.WidgetType.LIST)
                                .configuration("Bank Account List Widget")
                                .build())
                        .build())
                .build();
    }

    @Test
    void givenLayoutNotInRepo_whenGet_thenNullReturned() {
        ReflectionTestUtils.setField(layoutService, "layoutConfigName", "Layout");

        when(layoutRepository.findByObjectName("Layout")).thenReturn(Optional.empty());

        LayoutDto actualDto = layoutService.getConfiguration();

        assertNull(actualDto);
    }

    @Test
    void givenNoConfigurationName_whenGet_thenNullReturned() {
        ReflectionTestUtils.setField(layoutService, "layoutConfigName", "");

        LayoutDto actualDto = layoutService.getConfiguration();

        assertNull(actualDto);
    }
}