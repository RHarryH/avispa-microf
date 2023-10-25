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

package com.avispa.ecm.model.ui.layout.mapper;

import com.avispa.ecm.EcmConfiguration;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.format.Format;
import com.avispa.ecm.model.ui.layout.Layout;
import com.avispa.ecm.model.ui.layout.dto.LayoutDto;
import com.avispa.ecm.model.ui.layout.dto.SectionDto;
import com.avispa.ecm.model.ui.layout.dto.WidgetDto;
import com.avispa.ecm.model.ui.widget.list.ListWidgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@JsonTest
@Import({LayoutDtoMapper.class,
        EcmConfiguration.class} // required to load custom object mapper config
)
@Slf4j
class LayoutDtoMapperTest {
    public static final String CONFIGURATION_ID = "97ddd2fc-6cd4-4bfa-86bc-93d95e0a3a88";

    @Autowired
    private LayoutDtoMapper layoutDtoMapper;

    @MockBean
    private ListWidgetRepository listWidgetRepository;

    @Test
    void givenLayout_whenConvert_thenCorrectDtoCreated() {
        Layout layout = new Layout();
        layout.setObjectName("Layout");

        layout.setContents(Set.of(createContent("configuration/content/layout-content.json")));

        when(listWidgetRepository.findIdAndTypeNameByObjectName("Bank Account List Widget")).thenReturn(Optional.of(new ListWidgetRepository.ListWidgetProjection() {
            @Override
            public UUID getId() {
                return UUID.fromString(CONFIGURATION_ID);
            }

            @Override
            public String getTypeName() {
                return "Bank account";
            }
        }));

        LayoutDto actualDto = layoutDtoMapper.convert(layout);
        LayoutDto expectedDto = getExpectedLayoutDto(CONFIGURATION_ID);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void givenLayoutAndMissingConfigInRepo_whenConvert_thenDtoWithEmptyConfigurationCreated() {
        Layout layout = new Layout();
        layout.setObjectName("Layout");

        layout.setContents(Set.of(createContent("configuration/content/layout-content.json")));
        when(listWidgetRepository.findIdAndTypeNameByObjectName("Bank Account List Widget")).thenReturn(Optional.of(new ListWidgetRepository.ListWidgetProjection() {
            @Override
            public UUID getId() {
                return UUID.fromString(CONFIGURATION_ID);
            }

            @Override
            public String getTypeName() {
                return "Bank account";
            }
        }));

        LayoutDto actualDto = layoutDtoMapper.convert(layout);
        LayoutDto expectedDto = getExpectedLayoutDto("");

        assertEquals(expectedDto, actualDto);
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

    private static LayoutDto getExpectedLayoutDto(String config) {
        return LayoutDto.builder()
                .sections(List.of(SectionDto.builder()
                        .location(SectionDto.SectionLocation.SIDEBAR)
                        .widget(WidgetDto.builder()
                                .label("Repository")
                                .active(true)
                                .type(WidgetDto.WidgetType.REPOSITORY)
                                .build())
                        .widget(WidgetDto.builder()
                                .label("Bank Account")
                                .type(WidgetDto.WidgetType.LIST)
                                .configuration(config)
                                .resource("bank-account")
                                .configuration(CONFIGURATION_ID)
                                .build())
                        .build()))
                .build();
    }
}