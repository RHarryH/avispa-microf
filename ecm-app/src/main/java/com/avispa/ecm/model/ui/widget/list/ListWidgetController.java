package com.avispa.ecm.model.ui.widget.list;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/widget")
@RequiredArgsConstructor
@Slf4j
public class ListWidgetController {
    private final ListWidgetService listWidgetService;

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        //Do something additional if required
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/widgetError :: widgetError");
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.addObject("widgetId", "properties-widget");
        return modelAndView;
    }

    @GetMapping("/list-widget/{type}")
    public String getListWidget(@PathVariable("type") String resourceId, Model model) {
        model.addAttribute("listWidgetDto", listWidgetService.getAllDataFrom(resourceId));
        return "fragments/widgets/list-widget :: list-widget";
    }
}
