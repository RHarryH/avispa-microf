package com.avispa.microf.model.base;

import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.modal.context.ModalContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IModalableController<D extends Dto, C extends ModalContext<D>> {

    @GetMapping("/modal/add")
    ModelAndView getAddModal();

    @GetMapping("/modal/update/{id}")
    ModelAndView getUpdateModal(@PathVariable UUID id);

    @PostMapping(value = "/modal/add")
    @ResponseBody // it will just return status 200 when everything will go fine
    void addFromModal(@Valid @ModelAttribute("context") C context, BindingResult result);

    @PostMapping(value = "/modal/update/{id}")
    @ResponseBody
    void updateFromModal(@Valid @ModelAttribute("context") C context, @PathVariable("id") UUID id, BindingResult result);

    @GetMapping("/modal/page/{pageNumber}")
    ModelAndView loadPage(@PathVariable("pageNumber") int pageNumber, @ModelAttribute("context") C context);

    @PostMapping(value = "/modal/row/{tableName}")
    ModelAndView loadTableTemplate(@PathVariable("tableName") String tableName);
}
