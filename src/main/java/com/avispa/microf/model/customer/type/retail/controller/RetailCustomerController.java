package com.avispa.microf.model.customer.type.retail.controller;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.CustomerDto;
import com.avispa.microf.model.customer.mapper.CustomerMapper;
import com.avispa.microf.model.customer.service.CustomerService;
import com.avispa.microf.model.customer.type.retail.RetailCustomerDto;
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

        return modalService.constructModal(model, RetailCustomerDto.class, modal);
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200
    public void add(@Valid @ModelAttribute("ecmObject") RetailCustomerDto customerDto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        Customer customer = customerMapper.convertToEntity(customerDto);
        customerService.add(customer);
    }

    @GetMapping("/update/{id}")
    public String getCustomerUpdateModal(@PathVariable UUID id, Model model) {
        Customer customer = customerService.findById(id);
        CustomerDto<?> customerDto = customerMapper.convertToDto(customer);

        ModalConfiguration modal = ModalConfiguration.builder()
                .id("retail-customer-update-modal")
                .title("Update retail customer")
                .action("/customer/retail/update/" + id)
                .insert(false)
                .build();

        return modalService.constructModal(model, customerDto, modal);
    }

    @PostMapping(value = "/update/{id}")
    @ResponseBody
    public void update(@Valid @ModelAttribute("customer") RetailCustomerDto customerDto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        customerService.update(customerDto);
    }
}
