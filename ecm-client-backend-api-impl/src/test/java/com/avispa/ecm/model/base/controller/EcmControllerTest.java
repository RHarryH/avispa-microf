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

package com.avispa.ecm.model.base.controller;

import com.avispa.ecm.testdocument.simple.TestDocumentController;
import com.avispa.ecm.testdocument.simple.TestDocumentService;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(TestDocumentController.class)
class EcmControllerTest {
    @MockBean
    private TestDocumentService testDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void givenDto_whenAdd_thenServiceCalled() {
        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Dto");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/test-document")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(200));

        verify(testDocumentService).add(any(TestDocumentDto.class));
    }

    @Test
    @SneakyThrows
    void givenDto_whenUpdate_thenServiceCalled() {
        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Dto");

        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/test-document/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(200));

        verify(testDocumentService).update(any(TestDocumentDto.class), eq(id));
    }

    @Test
    @SneakyThrows
    void whenDelete_thenServiceCalled() {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/test-document/" + id))
                .andExpect(status().is(200));

        verify(testDocumentService).delete(id);
    }
}
