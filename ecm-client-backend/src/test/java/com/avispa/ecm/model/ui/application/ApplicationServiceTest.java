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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock
    private EcmConfigRepository<Application> applicationRepository;

    private final ApplicationDtoMapper applicationDtoMapper = Mappers.getMapper(ApplicationDtoMapper.class);

    private ApplicationService applicationService;

    @BeforeEach
    void init() {
        applicationService = new ApplicationService(applicationRepository, applicationDtoMapper);
    }

    @Test
    void givenApplicationInRepo_whenGet_thenReturned() {
        Application application = new Application();
        application.setObjectName("Application");
        application.setFullName("Full name");
        application.setShortName("Short name");
        application.setDescription("Description");

        ReflectionTestUtils.setField(applicationService, "applicationConfigName", "Application");

        when(applicationRepository.findByObjectName("Application")).thenReturn(Optional.of(application));

        ApplicationDto actualDto = applicationService.getConfiguration();
        ApplicationDto expectedDto = new ApplicationDto("Full name", "Short name", "Description");

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void givenApplicationNotInRepo_whenGet_thenDefaultReturned() {
        ReflectionTestUtils.setField(applicationService, "applicationConfigName", "Application");
        ReflectionTestUtils.setField(applicationService, "defaultAppFullName", "Default full name");
        ReflectionTestUtils.setField(applicationService, "defaultAppShortName", "Default short name");
        ReflectionTestUtils.setField(applicationService, "defaultAppDescription", "Default description");

        when(applicationRepository.findByObjectName("Application")).thenReturn(Optional.empty());

        ApplicationDto actualDto = applicationService.getConfiguration();
        ApplicationDto expectedDto = new ApplicationDto("Default full name", "Default short name", "Default description");

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void givenNoConfigurationName_whenGet_thenDefaultReturned() {
        ReflectionTestUtils.setField(applicationService, "applicationConfigName", "");
        ReflectionTestUtils.setField(applicationService, "defaultAppFullName", "Default full name");
        ReflectionTestUtils.setField(applicationService, "defaultAppShortName", "Default short name");
        ReflectionTestUtils.setField(applicationService, "defaultAppDescription", "Default description");

        ApplicationDto actualDto = applicationService.getConfiguration();
        ApplicationDto expectedDto = new ApplicationDto("Default full name", "Default short name", "Default description");

        assertEquals(expectedDto, actualDto);
    }
}