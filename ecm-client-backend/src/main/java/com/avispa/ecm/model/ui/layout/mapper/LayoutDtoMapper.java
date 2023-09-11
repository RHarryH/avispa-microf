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

import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.ui.layout.Layout;
import com.avispa.ecm.model.ui.layout.dto.LayoutDto;
import com.avispa.ecm.model.ui.layout.dto.WidgetDto;
import com.avispa.ecm.model.ui.widget.list.ListWidgetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class LayoutDtoMapper {
    private final ObjectMapper objectMapper;

    private final ListWidgetRepository listWidgetRepository;

    public LayoutDto convert(Layout entity) {
        return getLayoutContent(entity.getPrimaryContent()).orElseThrow();
    }

    private Optional<LayoutDto> getLayoutContent(Content content) {
        try {
            if(null == content) {
                return Optional.empty();
            }

            byte[] resource = Files.readAllBytes(Path.of(content.getFileStorePath()));
            LayoutDto dto = objectMapper.readerFor(LayoutDto.class).withRootName("layout").readValue(resource);
            if(null != dto) {
                convertConfigurationNameToUUID(dto);
            }

            return Optional.ofNullable(dto);
        } catch (IOException e) {
            log.error("Can't parse layout content from '{}'", content.getFileStorePath(), e);
        }

        return Optional.empty();
    }

    private void convertConfigurationNameToUUID(LayoutDto layoutDto) {
        layoutDto.getSections().forEach(section -> section.getWidgets().forEach(widgetDto -> {
            if (widgetDto.getType().equals(WidgetDto.WidgetType.LIST)) {
                widgetDto.setConfiguration(
                        getUUIDFromConfigurationName(widgetDto) // return empty string to prevent returning config name to frontend
                );
            }
        }));
    }

    private String getUUIDFromConfigurationName(WidgetDto widgetDto) {
        return listWidgetRepository.findIdByObjectName(widgetDto.getConfiguration())
                .map(UUID::toString).orElse("");
    }
}
