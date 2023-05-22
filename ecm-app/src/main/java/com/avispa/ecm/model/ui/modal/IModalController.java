package com.avispa.ecm.model.ui.modal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IModalController {

    @GetMapping("/add/{type}")
    ModelAndView getAddModal(@PathVariable("type") String typeIdentifier);

    @GetMapping("/clone/{type}")
    ModelAndView getCloneModal(@PathVariable("type") String typeIdentifier);

    @GetMapping("/update/{type}/{id}")
    ModelAndView getUpdateModal(@PathVariable("type") String typeIdentifier, @PathVariable("id") UUID id);

    @GetMapping("/page/{pageNumber}")
    ModelAndView loadPage(HttpServletRequest request, @PathVariable("pageNumber") int pageNumber);

    @PostMapping(value = "/row/{type}/{tableName}")
    ModelAndView loadTableTemplate(@PathVariable("type") String typeIdentifier, @PathVariable("tableName") String tableName);
}
