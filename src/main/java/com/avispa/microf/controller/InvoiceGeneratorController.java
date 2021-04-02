package com.avispa.microf.controller;

import com.avispa.microf.dto.InvoiceDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.service.invoice.file.IInvoiceFile;
import com.avispa.microf.service.invoice.file.ODFInvoiceFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
public class InvoiceGeneratorController {

    @Autowired
    InvoiceRepository invoiceRepository;

    @GetMapping("/invoice/generate")
    public String getForm(Model model) {
        model.addAttribute("invoice", new InvoiceDto());

        return "invoice/generate";
    }

    @PostMapping("/invoice/generate")
    public String saveInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto) {
        generate(invoiceDto);
        return "invoice/result";
    }

    @PostMapping(value = "/invoice/generate", params = "action=saveAndDownload", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] saveAndDownloadInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto) {
        return generate(invoiceDto);
    }

    private byte[] generate(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceDto.convertToEntity();
        try (IInvoiceFile invoiceFile = new ODFInvoiceFile(invoice)) {
            invoiceFile.generate();
            invoiceFile.save();
            invoiceRepository.save(invoice);
            return invoiceFile.asByteArray();
            /*if (print) {
                invoiceFile.print();
            }*/
        } catch (FileNotFoundException e) {
            log.error("Cannot open template file", e);
        } catch (IOException e) {
            log.error("Exception during invoice generation", e);
        } /*catch (PrinterException e) {
            log.error("Exception while printing", e);
        }*/

        return new byte[0];
    }
}
