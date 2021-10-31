package com.avispa.microf.model.widget;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.property.PropertyPageMapper;
import com.avispa.microf.service.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
    public String getPropertiesWidget(@PathVariable(required = false) UUID id, Model model) {
        if(null != id) {
            EcmObject ecmObject = ecmObjectRepository.findById(id).get();

            Map<String, Object> fields = null;
            try {
                fields = introspect(ecmObject);
            } catch (Exception e) {
                log.error("Exception: ", e);
            }

            model.addAttribute("properties", fields);

            PropertyPage propertyPage = (PropertyPage) contextService.getMatchingConfigurations(ecmObject).stream()
                    .filter(c -> c instanceof PropertyPage)
                    .findFirst()
                    .get();
            model.addAttribute("propertyPage", propertyPageMapper.convertToDto(propertyPage, ecmObject));
        } else {
            model.addAttribute("properties", new HashMap<>());
        }
        return "fragments/widgets/properties-widget :: properties-widget";
    }

    public static Map<String, Object> introspect(Object obj) throws Exception {
        Map<String, Object> result = new HashMap<>();
        BeanInfo info = Introspector.getBeanInfo(obj.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null) {
                result.put(pd.getName(), reader.invoke(obj));
            }
        }
        return result;
    }

}
