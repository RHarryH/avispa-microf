package com.avispa.microf.controller;

import com.avispa.ecm.model.folder.FolderRepository;
import com.avispa.microf.model.invoice.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final InvoiceRepository invoiceRepository;
    private final FolderRepository folderRepository;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("invoices", invoiceRepository.findAll());

        return "index"; //view
    }
}