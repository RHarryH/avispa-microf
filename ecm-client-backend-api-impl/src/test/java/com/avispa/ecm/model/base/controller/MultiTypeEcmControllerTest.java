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

import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentADto;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentController;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentDto;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentService;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentCommonDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(TestMultiDocumentController.class)
class MultiTypeEcmControllerTest {
    @MockBean
    private TestMultiDocumentService testMultiDocumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DtoService dtoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void givenDto_whenAdd_thenServiceCalled() {
        TestMultiDocumentCommonDto commonDto = new TestMultiDocumentCommonDto();
        commonDto.setType("A");

        TestMultiDocumentDto dto = new TestMultiDocumentADto();
        dto.setType("A");

        when(dtoService.convertCommonToConcreteDto(any(TestMultiDocumentCommonDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/test-document-multi")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonDto)))
                .andExpect(status().is(200));

        verify(testMultiDocumentService).add(any(TestMultiDocumentDto.class));
    }

    @Test
    @SneakyThrows
    void givenDto_whenUpdate_thenServiceCalled() {
        TestMultiDocumentCommonDto commonDto = new TestMultiDocumentCommonDto();
        commonDto.setType("A");

        TestMultiDocumentDto dto = new TestMultiDocumentADto();
        dto.setType("A");

        UUID id = UUID.randomUUID();

        when(dtoService.convertCommonToConcreteDto(any(TestMultiDocumentCommonDto.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/test-document-multi/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commonDto)))
                .andExpect(status().is(200));

        verify(testMultiDocumentService).update(any(TestMultiDocumentDto.class), eq(id));
    }

    @Test
    @SneakyThrows
    void whenDelete_thenServiceCalled() {
        UUID id = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/test-document-multi/" + id))
                .andExpect(status().is(200));

        verify(testMultiDocumentService).delete(id);
    }
}