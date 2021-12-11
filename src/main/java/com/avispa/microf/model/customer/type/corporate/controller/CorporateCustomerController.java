package com.avispa.microf.model.customer.type.corporate.controller;

import com.avispa.microf.model.customer.CustomerMapper;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomerDto;
import com.avispa.microf.model.customer.service.CustomerService;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/customer/corporate")
@RequiredArgsConstructor
@Slf4j
public class CorporateCustomerController {
    private final ModalService modalService;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @GetMapping("/add")
    public String getCustomerAddModal(Model model) {
        ModalConfiguration modal = ModalConfiguration.builder()
                .id("corporate-customer-add-modal")
                .title("Add new corporate customer")
                .action("/customer/corporate/add")
                .insert(true)
                .build();

        return modalService.constructModal(model, CorporateCustomer.class, CorporateCustomerDto.class, modal);
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200
    public void addInvoice(@ModelAttribute("ecmObject") CorporateCustomerDto customerDto) {
        CorporateCustomer customer = customerMapper.convertToEntity(customerDto);
        customerService.addCustomer(customer);
    }
}
