package com.avispa.microf.model.ui.widget;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.bankaccount.BankAccountService;
import com.avispa.microf.model.customer.CustomerService;
import com.avispa.microf.model.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    private final CustomerService customerService;
    private final BankAccountService bankAccountService;

    private final EcmObjectRepository<EcmObject> ecmObjectRepository;
    private final PropertyPageMapper propertyPageMapper;
    private final ContextService contextService;

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        //Do something additional if required
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/widgetError :: widgetError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("widgetId", "properties-widget");
        return modelAndView;
    }

    @GetMapping("/invoice-list-widget")
    public String getInvoiceListWidget(Model model) {
        model.addAttribute("invoices", invoiceService.findAll());
        return "fragments/widgets/invoice-list-widget :: invoice-list-widget";
    }

    @GetMapping("/customer-list-widget")
    public String getCustomerListWidget(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "fragments/widgets/customer-list-widget :: customer-list-widget";
    }

    @GetMapping("/bank-account-list-widget")
    public String getBankAccountListWidget(Model model) {
        model.addAttribute("bankAccounts", bankAccountService.findAll());
        return "fragments/widgets/bank-account-list-widget :: bank-account-list-widget";
    }

    @GetMapping("/repository-widget")
    public String getRepositoryWidget() {
        return "fragments/widgets/repository-widget :: repository-widget";
    }

    @GetMapping(value={"/properties-widget", "/properties-widget/{id}"})
    public String getPropertiesWidget(@PathVariable Optional<UUID> id, Model model) {
        id.flatMap(ecmObjectRepository::findById).ifPresentOrElse(ecmObject -> {
            PropertyPageContent propertyPageContent = contextService.getConfiguration(ecmObject, PropertyPage.class)
                    .map(propertyPage -> propertyPageMapper.convertToContent(propertyPage, ecmObject, true)) // convert to dto
                    .orElse(null); // return null otherwise

            model.addAttribute("propertyPage", propertyPageContent);
            model.addAttribute("context", ecmObject);
        },
        () -> model.addAttribute("nothingSelected", true)
        );
        return "fragments/widgets/properties-widget :: properties-widget";
    }
}
