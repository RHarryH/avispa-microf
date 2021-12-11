package com.avispa.microf.model.customer.type.retail.controller;

import com.avispa.microf.model.customer.CustomerMapper;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import com.avispa.microf.model.customer.type.retail.RetailCustomerDto;
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
@RequestMapping("/customer/retail")
@RequiredArgsConstructor
@Slf4j
public class RetailCustomerController {
    private final ModalService modalService;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @GetMapping("/add")
    public String getCustomerAddModal(Model model) {
        ModalConfiguration modal = ModalConfiguration.builder()
                .id("retail-customer-add-modal")
                .title("Add new retail customer")
                .action("/customer/retail/add")
                .insert(true)
                .build();

        return modalService.constructModal(model, RetailCustomer.class, RetailCustomerDto.class, modal);
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200
    public void addInvoice(@ModelAttribute("ecmObject") RetailCustomerDto customerDto) {
        RetailCustomer customer = customerMapper.convertToEntity(customerDto);
        customerService.addCustomer(customer);
    }
}
