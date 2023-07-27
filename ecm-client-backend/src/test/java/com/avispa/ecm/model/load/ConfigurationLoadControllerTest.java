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

import com.avispa.ecm.model.configuration.load.ConfigurationLoadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(ConfigurationLoadController.class)
class ConfigurationLoadControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigurationLoadService configurationLoadService;

    @Test
    void givenNonZipFile_whenLoad_thenReturn400() throws Exception {
        var file = new MockMultipartFile("configurationFile", "Test".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/configuration/load")
                    .file(file)
                    .param("overwrite", "false"))
                .andExpect(status().is(400));
    }

    @Test
    void givenEmptyZipFile_whenLoad_thenReturn400() throws Exception {
        var resource = ConfigurationLoadControllerTest.class.getClassLoader().getResource("zip/empty-zip.zip");
        var file = new MockMultipartFile("configurationFile", resource.openStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/configuration/load")
                        .file(file)
                        .param("overwrite", "false"))
                .andExpect(status().is(400));
    }

    @Test
    void givenCorrectZipFile_whenLoad_thenReturn201() throws Exception {
        var resource = ConfigurationLoadControllerTest.class.getClassLoader().getResource("zip/non-empty-zip.zip");
        var file = new MockMultipartFile("configurationFile", resource.openStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/configuration/load")
                        .file(file)
                        .param("overwrite", "false"))
                .andExpect(status().is(201));
    }
}