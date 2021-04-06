package com.avispa.microf.controller;

import com.avispa.microf.model.invoice.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private final InvoiceRepository invoiceRepository;

    @Autowired
    private IndexController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("invoices", invoiceRepository.findAll());

        return "index"; //view
    }

    // /hello?name=kotlin
    /*@GetMapping("/hello")
    public String mainWithParam(
            @RequestParam(name = "name", required = false, defaultValue = "")
                    String name, Model model) {

        model.addAttribute("message", name);

        return "index"; //view
    }*/
}