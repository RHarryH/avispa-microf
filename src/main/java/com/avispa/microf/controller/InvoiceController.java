package com.avispa.microf.controller;

import com.avispa.microf.dto.AttachementDto;
import com.avispa.microf.dto.InvoiceDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceMapper;
import com.avispa.microf.service.invoice.InvoiceService;
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
    private final InvoiceMapper invoiceMapper;

    @GetMapping("/add")
    public String getForm(Model model) {
        model.addAttribute("invoice", new InvoiceDto());

        return "invoice/add";
    }

    @PostMapping(value = "/add", params="submit")
    public String addInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.convertToEntity(invoiceDto);
        invoiceService.addInvoice(invoice);

        return "invoice/add-summary";
    }

    @PostMapping(value = "/add", params="cancel")
    public String cancelAdd() {
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        model.addAttribute("invoice", invoiceMapper.convertToDto(invoice));
        return "invoice/update";
    }

    @PostMapping(value = "/update/{id}", params="submit")
    public String update(@PathVariable UUID id, @ModelAttribute("invoice") InvoiceDto invoiceDto, BindingResult result) {
        // TODO: understand
        /*if (result.hasErrors()) {
            invoiceDto.setId(id);
            return "invoice/result";
        }*/

        invoiceService.updateInvoice(invoiceDto);
        return "invoice/update-summary";
    }

    @PostMapping(value = "/update/*", params="cancel")
    public String cancelUpdate() {
        return "redirect:/";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        invoiceService.deleteInvoice(id);

        return "redirect:/";
    }

    @GetMapping(value = "/rendition/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> download(@PathVariable UUID id) throws IOException {
        AttachementDto attachementDto = invoiceService.getRendition(id);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Path.of(attachementDto.getPath())));

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=" + attachementDto.getName())
                //.headers(headers)
                .contentLength(attachementDto.getSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
