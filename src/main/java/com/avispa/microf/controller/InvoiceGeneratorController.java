package com.avispa.microf.controller;

import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.service.invoice.file.IInvoiceFile;
import com.avispa.microf.service.invoice.file.ODFInvoiceFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
public class InvoiceGeneratorController {
    @GetMapping("/invoice/generate")
    public String invoiceForm(Model model) {
        model.addAttribute("invoice", new Invoice());

        return "invoice/generate";
    }

    @PostMapping("/invoice/generate")
    public String invoiceSubmit(@ModelAttribute Invoice invoice, Model model) {
        model.addAttribute("invoice", invoice);

        try (IInvoiceFile invoiceFile = new ODFInvoiceFile(invoice)) {
            invoiceFile.generate();
            invoiceFile.save();
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
        return "result";
    }
}
