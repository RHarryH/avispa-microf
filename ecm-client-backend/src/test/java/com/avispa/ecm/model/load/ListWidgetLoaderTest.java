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

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.load.dto.ListWidgetDto;
import com.avispa.ecm.model.load.dto.ListWidgetPropertyDto;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.model.ui.widget.list.ListWidget;
import com.avispa.ecm.model.ui.widget.list.ListWidgetProperty;
import com.avispa.ecm.model.ui.widget.list.ListWidgetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest
@ActiveProfiles("test")
class ListWidgetLoaderTest {

    @MockBean
    private ListWidgetRepository repository;

    @MockBean
    private TypeService typeService;

    @Autowired
    private ListWidgetLoader loader;

    @Captor
    private ArgumentCaptor<ListWidget> entityCaptor;

    @Test
    void givenListWidgetDtoAndContent_whenLoad_thenProperMethodsInvoked() {
        final String configName = "Default list widget config";

        var properties = List.of(ListWidgetPropertyDto.builder()
                        .name("A")
                        .build(),
                ListWidgetPropertyDto.builder()
                        .name("B")
                        .build());

        ListWidgetDto dto = new ListWidgetDto();
        dto.setName(configName);
        dto.setCaption("Caption");
        dto.setType("Document");
        dto.setEmptyMessage("Empty message");
        dto.setProperties(properties);

        Type type = new Type();
        type.setObjectName("Document");
        type.setEntityClass(Document.class);

        when(typeService.getType("Document")).thenReturn(type);
        when(repository.findByObjectName(configName)).thenReturn(Optional.empty());

        loader.load(dto, true);

        verify(repository).save(entityCaptor.capture());

        ListWidget listWidget = entityCaptor.getValue();
        assertEquals(configName, listWidget.getObjectName());
        assertEquals("Caption", listWidget.getCaption());
        assertEquals(type, listWidget.getType());
        assertEquals("Empty message", listWidget.getEmptyMessage());

        var expectedProperties = List.of(ListWidgetProperty.builder()
                        .name("A")
                        .build(),
                ListWidgetProperty.builder()
                        .name("B")
                        .build());

        assertEquals(expectedProperties, listWidget.getProperties());
    }
}