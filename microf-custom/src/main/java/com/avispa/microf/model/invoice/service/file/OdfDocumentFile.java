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

package com.avispa.microf.model.invoice.service.file;

import com.avispa.ecm.util.exception.EcmException;
import com.avispa.microf.model.invoice.service.file.data.FileData;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public abstract class OdfDocumentFile<T extends FileData> implements DocumentFile<T> {
    private static final Logger log = LoggerFactory.getLogger(OdfDocumentFile.class);

    protected final OdfTextDocument textDocument;

    protected OdfDocumentFile(String templatePath) {
        try {
            this.textDocument = OdfTextDocument.loadDocument(new File(templatePath));
        } catch (Exception e) {
            log.error("Unable to load document", e);
            throw new EcmException("Unable to load document");
        }
    }

    @Override
    public Path save(String path) {
        Path fileStorePath = Paths.get(path, UUID.randomUUID().toString());

        try(FileOutputStream out = new FileOutputStream(fileStorePath.toString())) {
            textDocument.save(out);
        } catch(Exception e) {
            log.error("Unable to save document", e);
        }

        return fileStorePath;
    }

    @Override
    public String getExtension() {
        return "odt";
    }

    @Override
    public void close() {
        if (null != textDocument) {
            textDocument.close();
        }
    }
}
