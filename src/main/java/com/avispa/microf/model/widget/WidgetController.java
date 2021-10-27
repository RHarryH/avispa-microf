package com.avispa.microf.model.widget;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.document.DocumentRepository;
import com.avispa.microf.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/widget")
@RequiredArgsConstructor
public class WidgetController {

    private final InvoiceService invoiceService;
    private final DocumentRepository documentRepository;

    @GetMapping("/invoice-list-widget")
    public String getInvoiceListWidget(Model model) {
        model.addAttribute("invoices", invoiceService.findAll());
        return "fragments/widgets/invoice-list-widget :: invoice-list-widget";
    }

    @GetMapping("/repository-widget")
    public String getRepositoryWidget() {
        return "fragments/widgets/repository-widget :: repository-widget";
    }

    @GetMapping(value={"/properties-widget", "/properties-widget/{id}"})
    public String getPropertiesWidget(@PathVariable(required = false) UUID id, Model model) {
        if(null != id) {
            model.addAttribute("properties", documentRepository.findById(id).map(Document::getObjectName).orElse("Document not found"));
        } else {
            model.addAttribute("properties", "Document not found");
        }
        return "fragments/widgets/properties-widget :: properties-widget";
    }

}
