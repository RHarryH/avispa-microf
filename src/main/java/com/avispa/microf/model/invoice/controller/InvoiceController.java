package com.avispa.microf.model.invoice.controller;

import com.avispa.microf.model.base.controller.BaseController;
import com.avispa.microf.model.content.ContentDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.invoice.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
@RequestMapping("/invoice")
public class InvoiceController extends BaseController<Invoice, InvoiceDto, InvoiceService> {

    public InvoiceController(InvoiceService service) {
        super(service);
    }

    @GetMapping(value = "/rendition/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> download(@PathVariable UUID id) throws IOException {
        ContentDto contentDto = getService().getRendition(id);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Path.of(contentDto.getPath())));

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=" + contentDto.getName())
                //.headers(headers)
                .contentLength(contentDto.getSize())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
