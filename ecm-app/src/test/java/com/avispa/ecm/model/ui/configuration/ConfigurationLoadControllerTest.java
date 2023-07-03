package com.avispa.ecm.model.ui.configuration;

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

        mockMvc.perform(MockMvcRequestBuilders.multipart("/configuration/load")
                    .file(file)
                    .param("overwrite", "false"))
                .andExpect(status().is(400));
    }

    @Test
    void givenEmptyZipFile_whenLoad_thenReturn400() throws Exception {
        var resource = ConfigurationLoadControllerTest.class.getClassLoader().getResource("zip/empty-zip.zip");
        var file = new MockMultipartFile("configurationFile", resource.openStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/configuration/load")
                        .file(file)
                        .param("overwrite", "false"))
                .andExpect(status().is(400));
    }

    @Test
    void givenCorrectZipFile_whenLoad_thenReturn201() throws Exception {
        var resource = ConfigurationLoadControllerTest.class.getClassLoader().getResource("zip/non-empty-zip.zip");
        var file = new MockMultipartFile("configurationFile", resource.openStream());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/configuration/load")
                        .file(file)
                        .param("overwrite", "false"))
                .andExpect(status().is(201));
    }
}