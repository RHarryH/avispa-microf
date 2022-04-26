package com.avispa.microf.model.invoice.controller;

import com.avispa.microf.model.base.BaseModalableController;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.content.ContentDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.invoice.InvoiceModalContext;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.service.InvoiceService;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalMode;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
@RequestMapping("/invoice")
public class InvoiceController extends BaseModalableController<Invoice, InvoiceDto, InvoiceService, InvoiceModalContext> {

    @Autowired
    public InvoiceController(InvoiceService invoiceService,
                             ModalService modalService) {
        super(invoiceService, modalService);
    }

    @Override
    public ModelAndView getAddModal() {
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id("invoice-add-modal")
                .title("Add new invoice")
                .action("/invoice/modal/add")
                .size("extra-large")
                .build();

        return getModal(modal);
    }

    @GetMapping("/modal/clone")
    public ModelAndView getCloneModal() {
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.CLONE)
                .id("invoice-clone-modal")
                .title("Clone existing invoice")
                .action("/invoice/modal/add")
                .size("extra-large")
                .build();

        return getModal(modal);
    }

    @Override
    public ModelAndView getUpdateModal(UUID id) {
        Invoice invoice = getService().findById(id);
        InvoiceDto invoiceDto = getEntityDtoMapper().convertToDto(invoice);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id("invoice-update-modal")
                .title("Update invoice")
                .action("/invoice/modal/update/" + id)
                .size("extra-large")
                .build();

        return getModal(invoiceDto, modal);
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

    @Override
    protected void registerTableFields(Map<String, Class<? extends Dto>> tableFieldsMap) {
        tableFieldsMap.put("positions", PositionDto.class);
    }
}
