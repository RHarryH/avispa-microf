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

package com.avispa.ecm.model.ui.application.mapper;

import com.avispa.ecm.EcmConfiguration;
import com.avispa.ecm.model.ui.application.Application;
import com.avispa.ecm.model.ui.application.dto.ApplicationDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@JsonTest
@ContextConfiguration(classes = {ApplicationDtoMapperImpl.class,
        EcmConfiguration.class}) // required to load custom object mapper config
@Slf4j
class ApplicationDtoMapperTest {
    @Autowired
    private ApplicationDtoMapper applicationDtoMapper;

    @Test
    void givenApplication_whenConvert_thenCorrectDtoCreated() {
        Application application = new Application();
        application.setObjectName("Application");
        application.setFullName("Full name");
        application.setShortName("Short name");
        application.setDescription("Description");

        ApplicationDto actualDto = applicationDtoMapper.convert(application);
        ApplicationDto expectedDto = getExpectedApplicationDto();

        assertEquals(expectedDto, actualDto);
    }

    private ApplicationDto getExpectedApplicationDto() {
        return new ApplicationDto("Full name", "Short name", "Description");
    }
}