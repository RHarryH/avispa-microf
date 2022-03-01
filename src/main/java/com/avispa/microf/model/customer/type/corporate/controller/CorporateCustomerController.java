package com.avispa.microf.model.customer.type.corporate.controller;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.model.customer.mapper.CustomerMapper;
import com.avispa.microf.model.customer.service.CustomerService;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomer;
import com.avispa.microf.model.customer.type.corporate.CorporateCustomerDto;
import com.avispa.microf.model.error.ErrorUtil;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.UUID;

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

        return modalService.constructModal(model, CorporateCustomerDto.class, modal);
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200
    public void add(@Valid @ModelAttribute("ecmObject") CorporateCustomerDto customerDto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        CorporateCustomer customer = customerMapper.convertToEntity(customerDto);
        customerService.add(customer);
    }

    @GetMapping("/update/{id}")
    public String getCustomerUpdateModal(@PathVariable UUID id, Model model) {
        Customer customer = customerService.findById(id);
        CustomerDto<?> customerDto = customerMapper.convertToDto(customer);

        ModalConfiguration modal = ModalConfiguration.builder()
                .id("corporate-customer-update-modal")
                .title("Update corporate customer")
                .action("/customer/corporate/update/" + id)
                .insert(false)
                .build();

        return modalService.constructModal(model, customerDto, modal);
    }

    @PostMapping(value = "/update/{id}")
    @ResponseBody
    public void update(@Valid @ModelAttribute("customer") CorporateCustomerDto customerDto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        customerService.update(customerDto);
    }
}
