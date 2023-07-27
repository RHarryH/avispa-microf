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

package com.avispa.ecm.model.ui.menu;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.ui.menu.dto.MenuDto;
import com.avispa.ecm.model.ui.menu.mapper.MenuDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class MenuService {
    private final EcmConfigRepository<Menu> menuRepository;
    private final MenuDtoMapper menuDtoMapper;

    @Value("${avispa.ecm.client.configuration.menu:}")
    private String menuConfigName;

    public MenuDto getConfiguration() {
        if(Strings.isBlank(menuConfigName)) {
            return null;
        }

        return menuRepository.findByObjectName(menuConfigName)
                .map(menuDtoMapper::convert)
                .orElse(null);
    }
}
