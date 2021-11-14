package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.invoice.InvoiceMapper;
import com.avispa.microf.model.property.PropertyPageDto;
import com.avispa.microf.model.property.mapper.PropertyPageMapper;
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
@RequestMapping("/ui")
@RequiredArgsConstructor
public class ModalController {
    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    private final ContextService contextService;
    private final PropertyPageMapper propertyPageMapper;

    @GetMapping("/invoice-add")
    public String getInvoiceListWidget(Model model) {
        InvoiceDto invoiceDto = new InvoiceDto();

        PropertyPageDto propertyPageDto = getInvoicePropertyPage(invoiceDto);

        model.addAttribute("propertyPage", propertyPageDto);
        model.addAttribute("object", invoiceDto);

        Modal modal = Modal.builder()
                .id("invoice-add-modal")
                .title("Add new invoice")
                .action("/invoice/add")
                .insert(true)
                .build();
        model.addAttribute("modal", modal);
        return "fragments/modal :: upsertModal";
    }

    @GetMapping("/invoice-update/{id}")
    public String getInvoiceListWidget(@PathVariable UUID id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        InvoiceDto invoiceDto = invoiceMapper.convertToDto(invoice);

        PropertyPageDto propertyPageDto = getInvoicePropertyPage(invoiceDto);

        model.addAttribute("propertyPage", propertyPageDto);
        model.addAttribute("object", invoiceDto);

        Modal modal = Modal.builder()
                .id("invoice-update-modal")
                .title("Update invoice")
                .action("/invoice/update/" + id)
                .insert(false)
                .build();
        model.addAttribute("modal", modal);
        return "fragments/modal :: upsertModal";
    }

    private PropertyPageDto getInvoicePropertyPage(InvoiceDto invoiceDto) {
        return contextService.getConfiguration(Invoice.class, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.convertToDto(propertyPage, invoiceDto, false)) // convert to dto
                .orElse(null);
    }
}
