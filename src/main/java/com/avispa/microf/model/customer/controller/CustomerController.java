package com.avispa.microf.model.customer.controller;

import com.avispa.microf.model.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") UUID id) {
        customerService.delete(id);
    }
}
