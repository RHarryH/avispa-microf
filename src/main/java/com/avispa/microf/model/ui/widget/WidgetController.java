package com.avispa.microf.model.ui.widget;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import com.avispa.ecm.model.context.ContextService;
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

    private final EcmObjectRepository<EcmObject> ecmObjectRepository;
    private final PropertyPageMapper propertyPageMapper;
    private final ContextService contextService;

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        //Do something additional if required
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/widgetError :: widgetError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("widgetId", "properties-widget"); // TODO:
        return modelAndView;
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
