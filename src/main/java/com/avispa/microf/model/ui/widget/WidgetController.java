package com.avispa.microf.model.ui;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.property.PropertyPageDto;
import com.avispa.microf.model.property.mapper.PropertyPageMapper;
import com.avispa.microf.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/widget")
@RequiredArgsConstructor
@Slf4j
public class WidgetController {

    private final InvoiceService invoiceService;
    private final EcmObjectRepository<EcmObject> ecmObjectRepository;
    private final PropertyPageMapper propertyPageMapper;
    private final ContextService contextService;

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
    public String getPropertiesWidget(@PathVariable Optional<UUID> id, Model model) {
        id.flatMap(ecmObjectRepository::findById).ifPresentOrElse(ecmObject -> {
            PropertyPageDto propertyPageDto = contextService.getConfiguration(ecmObject, PropertyPage.class)
                    .map(propertyPage -> propertyPageMapper.convertToDto(propertyPage, ecmObject, true)) // convert to dto
                    .orElse(null); // return null otherwise

            model.addAttribute("propertyPage", propertyPageDto);
            model.addAttribute("ecmObject", ecmObject);
        },
        () -> model.addAttribute("nothingSelected", true)
        );
        return "fragments/widgets/properties-widget :: properties-widget";
    }
}
