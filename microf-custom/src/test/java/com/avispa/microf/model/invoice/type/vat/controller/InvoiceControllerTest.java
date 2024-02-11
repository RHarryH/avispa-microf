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

package com.avispa.microf.model.invoice.type.vat.controller;

import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.content.ContentDto;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.type.vat.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {
    public static final String CONFIGURATION_ID = "97ddd2fc-6cd4-4bfa-86bc-93d95e0a3a88";

    @MockBean
    private InvoiceService service;

    @MockBean
    private DtoService dtoService;

    @MockBean
    private CounterStrategy counterStrategy;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenContent_whenDownload_thenReturn200AndContentDisposition() throws Exception {
        ContentDto contentDto = new ContentDto();

        contentDto.setName("Test file.pdf");
        contentDto.setPath(getTestFilePath());

        when(service.getRendition(UUID.fromString(CONFIGURATION_ID))).thenReturn(contentDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/invoice/rendition/" + CONFIGURATION_ID))
                .andExpect(status().is(200))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, is("attachment; filename=Test file.pdf")));
    }

    @Test
    void givenNoContent_whenDownload_thenReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/invoice/rendition/" + CONFIGURATION_ID))
                .andExpect(status().is(404));
    }

    private String getTestFilePath() throws URISyntaxException {
        URL res = getClass().getClassLoader().getResource("test.odt");
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }
}