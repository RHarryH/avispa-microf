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

package com.avispa.ecm.model.ui.application;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.ui.application.dto.ApplicationDto;
import com.avispa.ecm.model.ui.application.mapper.ApplicationDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final EcmConfigRepository<Application> applicationRepository;
    private final ApplicationDtoMapper applicationDtoMapper;

    @Value("${avispa.ecm.client.configuration.application:}")
    private String applicationConfigName;

    @Value("${avispa.ecm.client.name}")
    private String defaultAppFullName;

    @Value("${avispa.ecm.client.short-name}")
    private String defaultAppShortName;

    @Value("${avispa.ecm.client.description}")
    private String defaultAppDescription;

    public ApplicationDto getConfiguration() {
        if(Strings.isBlank(applicationConfigName)) {
            return getDefaultApplicationDto();
        }

        return applicationRepository.findByObjectName(applicationConfigName)
                .map(applicationDtoMapper::convert)
                .orElseGet(this::getDefaultApplicationDto);
    }

    private ApplicationDto getDefaultApplicationDto() {
        return new ApplicationDto(defaultAppFullName, defaultAppShortName, defaultAppDescription);
    }
}
