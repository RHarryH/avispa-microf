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

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.document.DocumentService;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.folder.FolderService;
import com.avispa.ecm.model.zip.ZipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("directory")
@RequiredArgsConstructor
@Slf4j
public class DirectoryController {

    private final FolderService folderService;
    private final DocumentService documentService;
    private final ZipService zipService;
    private final DirectoryNodeMapper directoryNodeMapper;

    @GetMapping
    public List<DirectoryNode> directory() {
        List<DirectoryNode> directoryNodes = new ArrayList<>();

        processFolders(directoryNodes);
        processDocuments(directoryNodes);

        return directoryNodes;
    }

    private void processDocuments(List<DirectoryNode> directoryNodes) {
        List<Document> documents = documentService.getAllDocumentsInAnyFolder();
        for(Document document : documents) {
            directoryNodes.add(directoryNodeMapper.convert(document));
        }
    }

    private void processFolders(List<DirectoryNode> directoryNodes) {
        List<Folder> folders = folderService.getAllFolders();
        for(Folder folder : folders) {
            directoryNodes.add(directoryNodeMapper.convert(folder));
        }
    }

    @GetMapping(params = "export", produces = "application/zip")
    public ResponseEntity<Resource> exportStructure() {
        ByteArrayResource resource = new ByteArrayResource(zipService.getZippedStructure());

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=structure-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + ".zip")
                .contentLength(resource.contentLength())
                .body(resource);
    }
}