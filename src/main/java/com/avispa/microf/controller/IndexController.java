package com.avispa.microf.controller;

import com.avispa.microf.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final InvoiceService invoiceService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("invoices", invoiceService.findAll());

        return "index"; //view
    }
}