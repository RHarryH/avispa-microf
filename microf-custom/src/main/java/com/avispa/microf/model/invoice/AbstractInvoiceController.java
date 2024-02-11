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

package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.base.controller.BaseSimpleEcmController;
import com.avispa.ecm.model.content.ContentDto;
import com.avispa.ecm.util.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public abstract class AbstractInvoiceController<I extends BaseInvoice, D extends BaseInvoiceDto, S extends AbstractInvoiceService<I, D, ? extends EcmObjectRepository<I>, ? extends AbstractInvoiceMapper<I, D>>> extends BaseSimpleEcmController<I, D, S> {

    protected AbstractInvoiceController(S service) {
        super(service);
    }

    @Override
    protected void add(D dto) {
        service.add(dto);
    }

    @Override
    protected void update(UUID id, D dto) {
        service.update(dto, id);
    }

    @GetMapping(value = "/rendition/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    @CrossOrigin(exposedHeaders = HttpHeaders.CONTENT_DISPOSITION)
    @Operation(summary = "Returns pdf rendition of a document.")
    @ApiResponse(responseCode = "200", description = "Rendition is ready to download", content = @Content)
    @ApiResponse(responseCode = "404", description = "Document with provided id or its rendition does not exist", content = @Content)
    public ResponseEntity<Resource> download(
            @PathVariable
            @Parameter(description = "id of the document")
            UUID id) {
        ContentDto contentDto = service.getRendition(id);
        if(null == contentDto) {
            throw new ResourceNotFoundException();
        }

        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Path.of(contentDto.getPath())));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + contentDto.getName())
                    .contentLength(contentDto.getSize())
                    .body(resource);
        } catch (IOException e) {
            log.error("Can't find requested resource", e);
            throw new ResourceNotFoundException();
        }
    }
}
