package com.avispa.microf.controller;

import com.avispa.cms.model.content.Content;
import com.avispa.cms.model.content.ContentRepository;
import com.avispa.cms.model.filestore.FileStore;
import com.avispa.cms.service.rendition.RenditionService;
import com.avispa.microf.dto.InvoiceDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.service.invoice.file.IInvoiceFile;
import com.avispa.microf.service.invoice.file.ODFInvoiceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.avispa.cms.util.Formats.PDF;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final ContentRepository contentRepository;
    private final RenditionService renditionService;
    private final FileStore fileStore;
    //private InvoiceMapper invoiceMapper;

    @GetMapping("/add")
    public String getForm(Model model) {
        model.addAttribute("invoice", new InvoiceDto());

        return "invoice/add";
    }

    @PostMapping("/add")
    public String addInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto) {
        add(invoiceDto);
        return "invoice/add_summary";
    }

    private void add(InvoiceDto invoiceDto) {
        Invoice invoice = convertToEntity(invoiceDto);
        try (IInvoiceFile invoiceFile = new ODFInvoiceFile(invoice)) {
            invoiceFile.generate();
            Content content = invoiceFile.save(fileStore.getRootPath());
            content.setDocument(invoice);
            invoiceRepository.save(invoice);

            renditionService.generate(content);
        } catch (FileNotFoundException e) {
            log.error("Cannot open template file", e);
        } catch (IOException e) {
            log.error("Exception during invoice generation", e);
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable long id, Model model) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(InvoiceNotFoundException::new);
        model.addAttribute("invoice", convertToDto(invoice));
        return "invoice/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("invoice") InvoiceDto invoiceDto, BindingResult result) {
        // TODO: understand
        /*if (result.hasErrors()) {
            invoiceDto.setId(id);
            return "invoice/result";
        }*/

        Invoice invoice = convertToEntity(invoiceDto);
        invoiceRepository.save(invoice);
        return "invoice/edit_summary";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(InvoiceNotFoundException::new);

        invoiceRepository.delete(invoice);

        return "redirect:/";
    }

    /*@PostMapping(value = "/invoice/generate", params = "action=saveAndDownload", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] saveAndDownloadInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto) {
        return generate(invoiceDto);
    }*/

    @GetMapping(value = "/rendition/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> download(@PathVariable long id) throws IOException {
        Invoice invoice = invoiceRepository.getOne(id);
        Content content = contentRepository.findByDocumentIdAndExtension(invoice.getId(), PDF);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Path.of(content.getFileStorePath())));

        String renditionName = invoice.getObjectName().replace("/","_") + "." + PDF;

        return ResponseEntity.ok()
                .header("Content-disposition", "attachment; filename=" + renditionName)
                //.headers(headers)
                .contentLength(content.getSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    public Invoice convertToEntity(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();

        invoice.setId(invoiceDto.getId());
        invoice.setSerialNumber(invoiceDto.getSerialNumber());
        invoice.setInvoiceDate(invoiceDto.getInvoiceDate());
        invoice.setServiceDate(invoiceDto.getServiceDate());
        invoice.setNetValue(new BigDecimal(invoiceDto.getNetValue().replace(",", ".").replace(" ", "")));
        invoice.setComments(invoiceDto.getComments());

        invoice.computeIndirectValues();
        return invoice;
    }

    public InvoiceDto convertToDto(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();

        invoiceDto.setId(invoice.getId());
        invoiceDto.setSerialNumber(invoice.getSerialNumber());
        invoiceDto.setInvoiceDate(invoice.getInvoiceDate());
        invoiceDto.setServiceDate(invoice.getServiceDate());
        invoiceDto.setNetValue(invoice.getNetValue().toPlainString());
        invoiceDto.setComments(invoice.getComments());

        invoice.computeIndirectValues();
        return invoiceDto;
    }
}
