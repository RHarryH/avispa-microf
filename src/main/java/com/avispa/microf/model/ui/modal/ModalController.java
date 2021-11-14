package com.avispa.microf.model.ui;

import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.property.PropertyPageDto;
import com.avispa.microf.model.property.mapper.PropertyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class UpsertController {
    private final ContextService contextService;
    private final PropertyPageMapper propertyPageMapper;

    @GetMapping("/invoice-add")
    public String getInvoiceListWidget(Model model) {
        InvoiceDto invoiceDto = new InvoiceDto();

        PropertyPageDto propertyPageDto = contextService.getConfiguration(Invoice.class, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.convertToDto(propertyPage, invoiceDto, false)) // convert to dto
                .orElse(null); // return null otherwise

        model.addAttribute("propertyPage", propertyPageDto);
        model.addAttribute("ecmObject", invoiceDto);

        ModalDto modalDto = ModalDto.builder()
                .id("invoice-add-modal")
                .title("Add new invoice")
                .action("/invoice/add")
                .insert(true)
                .build();
        model.addAttribute("modal", modalDto);
        return "fragments/modal :: upsertModal";
    }
}
