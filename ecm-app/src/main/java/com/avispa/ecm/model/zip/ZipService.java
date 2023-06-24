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

package com.avispa.ecm.model.zip;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.folder.Folder;
import com.avispa.ecm.model.folder.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ZipService {
    private final FolderService folderService;

    public byte[] getZippedStructure() {
        List<Folder> rootFolders = folderService.getRootFolders();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try(final ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            for(Folder root : rootFolders) {
                insertFolder(zipOut, root.getPath());
                List<EcmObject> objects = folderService.getAllFoldersAndLinkedObjects(root, true);
                for(EcmObject object : objects) {
                    if(object instanceof Document) {
                        Document document = (Document)object;
                        Content content = document.getPrimaryContent();
                        String documentPath = Paths.get(document.getFolder().getPath().substring(1), content.getObjectName()).toString();

                        insertDocument(zipOut, documentPath, content.getFileStorePath());
                    } else if(object instanceof Folder) {
                        Folder folder = (Folder)object;
                        insertFolder(zipOut, folder.getPath());
                    }

                }
            }
        } catch (IOException e) {
            log.error("Error when zipping directory structure", e);
        }

        return baos.toByteArray();
    }

    private void insertFolder(ZipOutputStream zipOut, String folderPath) throws IOException {
        zipOut.putNextEntry(new ZipEntry(folderPath.substring(1) + File.separator));
        zipOut.closeEntry();
    }

    private void insertDocument(ZipOutputStream zipOut, String documentPath, String contentPath) throws IOException {
        zipOut.putNextEntry(new ZipEntry(documentPath));
        byte[] bytes = Files.readAllBytes(Paths.get(contentPath));
        zipOut.write(bytes, 0, bytes.length);
        zipOut.closeEntry();
    }
}
