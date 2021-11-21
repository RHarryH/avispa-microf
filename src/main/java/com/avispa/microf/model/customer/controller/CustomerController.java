package com.avispa.microf.model.customer.controller;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final ModalService modalService;

    @GetMapping("/add")
    public String getCustomerAddModal(Model model) {
        ModalConfiguration modal = ModalConfiguration.builder()
                .id("customer-add-modal")
                .title("Add new customer")
                .action("/customer/add")
                .insert(true)
                .build();

        return modalService.constructModal(model, Customer.class, CustomerDto.class, modal);
    }
}
