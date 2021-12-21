package com.avispa.microf.model.invoice.controller;

import com.avispa.microf.model.content.ContentDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.invoice.InvoiceMapper;
import com.avispa.microf.model.invoice.service.InvoiceService;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ModalService modalService;
    private final InvoiceMapper invoiceMapper;

    @GetMapping("/add")
    public String getInvoiceAddModal(Model model) {
        ModalConfiguration modal = ModalConfiguration.builder()
                .id("invoice-add-modal")
                .title("Add new invoice")
                .action("/invoice/add")
                .insert(true)
                .build();

        return modalService.constructModal(model, Invoice.class, InvoiceDto.class, modal);
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200
    public void add(@ModelAttribute("ecmObject") InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.convertToEntity(invoiceDto);
        invoiceService.add(invoice);
    }

    @GetMapping("/update/{id}")
    public String getInvoiceUpdateModal(@PathVariable UUID id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        InvoiceDto invoiceDto = invoiceMapper.convertToDto(invoice);

        ModalConfiguration modal = ModalConfiguration.builder()
                .id("invoice-update-modal")
                .title("Update invoice")
                .action("/invoice/update/" + id)
                .insert(false)
                .build();

        return modalService.constructModal(model, Invoice.class, invoiceDto, modal);
    }

    @PostMapping(value = "/update/{id}")
    @ResponseBody
    public void update(@PathVariable UUID id, @ModelAttribute("invoice") InvoiceDto invoiceDto, BindingResult result) {
        // TODO: understand
        /*if (result.hasErrors()) {
            invoiceDto.setId(id);
            return "invoice/result";
        }*/

        invoiceService.update(invoiceDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") UUID id) {
        invoiceService.delete(id);
    }

    @GetMapping(value = "/rendition/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> download(@PathVariable UUID id) throws IOException {
        ContentDto contentDto = invoiceService.getRendition(id);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Path.of(contentDto.getPath())));

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=" + contentDto.getName())
                //.headers(headers)
                .contentLength(contentDto.getSize())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
