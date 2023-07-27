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

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.load.dto.LayoutDto;
import com.avispa.ecm.model.ui.layout.Layout;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest
@ActiveProfiles("test")
class LayoutLoaderTest {

    @MockBean(name = "layoutRepository")
    private EcmConfigRepository<Layout> repository;

    @MockBean
    private ContentService contentService;

    @Autowired
    private LayoutLoader loader;

    @Captor
    private ArgumentCaptor<Layout> entityCaptor;

    @Value("classpath:configuration/content/layout-content.json")
    Resource resourceFile;

    @Test
    void givenLayoutDtoAndContent_whenLoad_thenProperMethodsInvoked() throws IOException {
        final String configName = "Default layout config";
        LayoutDto dto = new LayoutDto();
        dto.setName(configName);

        when(repository.findByObjectName(configName)).thenReturn(Optional.empty());

        Path contentPath = resourceFile.getFile().toPath();

        loader.load(dto, contentPath, true);

        verify(contentService).loadContentOf(entityCaptor.capture(), any(Path.class), eq(true));
        verify(repository).save(entityCaptor.getValue());
    }
}