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

package com.avispa.ecm.model.directory;

import com.avispa.ecm.model.document.DocumentService;
import com.avispa.ecm.model.folder.FolderService;
import com.avispa.ecm.model.zip.ZipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.avispa.ecm.util.FolderCreationUtils.getDocument;
import static com.avispa.ecm.util.FolderCreationUtils.getFolder;
import static com.avispa.ecm.util.FolderCreationUtils.getRoot;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rafał Hiszpański
 */
@WebMvcTest(DirectoryController.class)
@Import({ZipService.class, DirectoryNodeMapperImpl.class, DocumentService.class, FolderService.class})
class DirectoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DirectoryNodeMapper directoryNodeMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FolderService folderService;

    @MockBean
    private DocumentService documentService;

    @Test
    void givenDirectory_whenExecute_thenReturn200AndContentMatches() throws Exception {
        // given
        var root = getRoot();
        var folder = getFolder(root, "Folder");
        var document = getDocument(root);
        var document2 = getDocument(folder);

        when(folderService.getAllFolders()).thenReturn(List.of(root, folder));
        when(folderService.getRootFolders()).thenReturn(List.of(root));
        when(folderService.getAllFoldersAndLinkedObjects(root, true)).thenReturn(List.of(folder, document, document2));
        when(documentService.getAllDocumentsInAnyFolder()).thenReturn(List.of(document, document2));

        var expected = objectMapper.writeValueAsString(List.of(
                directoryNodeMapper.convert(root),
                directoryNodeMapper.convert(folder),
                directoryNodeMapper.convert(document),
                directoryNodeMapper.convert(document2)
        ));

        MvcResult actualResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/directory"))
                .andExpect(status().is(200))
                .andReturn();

        assertEquals(expected, actualResult.getResponse().getContentAsString());
    }

    @Test
    void givenDirectory_whenExport_thenReturn200AndContentDisposition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/directory/export"))
                .andExpect(status().is(200))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, startsWith("attachment; filename=structure-")));
    }
}