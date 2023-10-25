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

package com.avispa.ecm.model.ui.widget;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
import com.avispa.ecm.model.ui.widget.list.ListWidget;
import com.avispa.ecm.model.ui.widget.list.ListWidgetRepository;
import com.avispa.ecm.model.ui.widget.list.ListWidgetService;
import com.avispa.ecm.util.error.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static com.avispa.ecm.util.FolderCreationUtils.getDocument;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(WidgetController.class)
class WidgetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EcmObjectRepository<EcmObject> ecmObjectRepository;

    @MockBean
    private PropertyPageService propertyPageService;

    @MockBean
    private ContextService contextService;

    @MockBean
    private ListWidgetRepository listWidgetRepository;

    @MockBean
    private ListWidgetService listWidgetService;

    @Test
    void givenPropertyPageAndObject_whenLoadPropertiesWidget_thenReturn200() throws Exception {
        UUID objectId = UUID.randomUUID();
        var sampleContent = new PropertyPageContent();

        when(ecmObjectRepository.findById(objectId)).thenReturn(Optional.of(getDocument()));
        when(contextService.getConfiguration(any(EcmObject.class), eq(PropertyPage.class))).thenReturn(Optional.of(new PropertyPage()));
        when(propertyPageService.getPropertyPage(any(), eq(true))).thenReturn(sampleContent);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/widget/properties-widget/" + objectId))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.objectFound", is(true)))
                .andExpect(jsonPath("$.propertyPage", notNullValue()));
    }

    @Test
    void givenNonExitingPropertyPage_whenLoadPropertiesWidget_thenReturn404() throws Exception {
        UUID objectId = UUID.randomUUID();

        when(propertyPageService.getPropertyPage(any(), eq(true))).thenThrow(new ResourceNotFoundException());
        when(ecmObjectRepository.findById(objectId)).thenReturn(Optional.of(getDocument()));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/widget/properties-widget/" + objectId))
                .andExpect(status().is(404));
    }

    @Test
    void givenNonExitingId_whenLoadPropertiesWidget_thenObjectNotFound() throws Exception {
        UUID objectId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/widget/properties-widget/" + objectId))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.objectFound", is(false)));
    }

    @Test
    void givenWidgetConfig_whenLoadListWidget_thenReturn200() throws Exception {
        UUID objectId = UUID.randomUUID();
        ListWidget listWidget = new ListWidget();

        when(listWidgetRepository.findById(objectId)).thenReturn(Optional.of(listWidget));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/widget/list-widget/" + objectId))
                .andExpect(status().is(200));

        verify(listWidgetService).getAllDataFrom(any(ListWidget.class));
    }

    @Test
    void givenNonExistingConfig_whenLoadListWidget_thenReturn404() throws Exception {
        UUID objectId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/widget/list-widget/" + objectId))
                .andExpect(status().is(404));
    }
}