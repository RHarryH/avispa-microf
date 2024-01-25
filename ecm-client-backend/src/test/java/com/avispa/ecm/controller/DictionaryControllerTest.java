/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.ecm.controller;

import com.avispa.ecm.model.configuration.propertypage.content.control.dictionary.DynamicLoad;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.DictionaryControlLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(DictionaryController.class)
class DictionaryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DictionaryControlLoader dictionaryControlLoader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void dictionaryLoadTest() throws Exception {
        var dictionaryDto = new DictionaryController.DictionaryDto("{\"$limit\": 5}", null);
        var dynamicLoad = new DynamicLoad("Test document", "{\"$limit\": 5}");

        when(dictionaryControlLoader.loadDynamicDictionary(eq(dynamicLoad), any())).thenReturn(Map.of("a", "B", "c", "D"));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/dictionary/test-document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dictionaryDto)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.a", is("B")))
                .andExpect(jsonPath("$.c", is("D")));
    }
}