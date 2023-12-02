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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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
    private static final int ZIP_HEADER_MAGIC_NUMBER = 0x504B0304;
    private final FolderService folderService;

    /**
     * Extracts all folders and documents and return them as a byte array representing a zip file.
     * Empty folders are not included.
     * @return
     */
    public byte[] getZippedStructure() {
        List<Folder> rootFolders = folderService.getRootFolders();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try(final ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            for(Folder root : rootFolders) {
                List<EcmObject> objects = folderService.getAllFoldersAndLinkedObjects(root, true);
                for(EcmObject object : objects) {
                    if (object instanceof Document document) {
                        Content content = document.getPrimaryContent();
                        String documentPath = Paths.get(document.getFolder().getPath().substring(1), content.getObjectName()).toString();

                        insertDocument(zipOut, documentPath, content.getFileStorePath());
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error when zipping directory structure", e);
        }

        return baos.toByteArray();
    }

    /*private void insertFolder(ZipOutputStream zipOut, String folderPath) throws IOException {
        zipOut.putNextEntry(new ZipEntry(folderPath.substring(1) + "/")); // don't use File.separator as it creates empty files
        zipOut.closeEntry();
    }*/

    private void insertDocument(ZipOutputStream zipOut, String documentPath, String contentPath) throws IOException {
        zipOut.putNextEntry(new ZipEntry(documentPath));
        if(StringUtils.isNotEmpty(contentPath)) {
            byte[] bytes = Files.readAllBytes(Paths.get(contentPath));
            zipOut.write(bytes, 0, bytes.length);
        }
        zipOut.closeEntry();
    }

    /**
     * Checks if file is a zip file by checking its magic number. It checks only if it is non spanned and non empty
     * archive. For more details please see below article:
     * <a href="https://en.wikipedia.org/wiki/List_of_file_signatures">List of file signatures</a>
     * @param file
     * @return
     */
    public static boolean isZipFile(File file) {
        if(null == file) {
            throw new IllegalStateException("File can't be null");
        }

        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            return compareMagicNumber(raf);
        } catch (FileNotFoundException e) {
            log.error("File '{}' does not exist", file.getPath(), e);
        } catch (IOException e) {
            log.error("File '{}' can't be read", file.getPath(), e);
        }

        return false;
    }

    public static boolean isZipFile(InputStream inputStream) {
        if(null == inputStream) {
            throw new IllegalStateException("Stream can't be null");
        }

        try(var dis = new DataInputStream(inputStream)) {
            return compareMagicNumber(dis);
        } catch (IOException e) {
            log.error("Stream can't be read", e);
        }

        return false;
    }

    private static boolean compareMagicNumber(DataInput dataInput) throws IOException {
        return dataInput.readInt() == ZIP_HEADER_MAGIC_NUMBER;
    }
}
