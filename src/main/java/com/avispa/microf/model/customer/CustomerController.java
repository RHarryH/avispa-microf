package com.avispa.microf.model.customer;

import com.avispa.microf.model.base.controller.BaseController;
import com.avispa.microf.model.customer.exception.CustomerInUseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController<Customer, CustomerDto, CustomerService> {

    public CustomerController(CustomerService service) {
        super(service);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") UUID id) {
        try {
            super.delete(id);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Customer '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new CustomerInUseException(errorMessage);
        }
    }
}
