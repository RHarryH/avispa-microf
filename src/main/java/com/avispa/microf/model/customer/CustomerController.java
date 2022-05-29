package com.avispa.microf.model.customer;

import com.avispa.microf.model.base.controller.BaseController;
import com.avispa.microf.model.customer.corporate.CorporateCustomerDto;
import com.avispa.microf.model.customer.exception.CustomerInUseException;
import com.avispa.microf.model.customer.retail.RetailCustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
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
    protected CustomerDto createDto(Map<String, Object> object) {
        CustomerDto dto = null;
        // TODO: discriminator rules?
        if(object.containsKey("type")) {
            Object type = object.get("type");
            if ("CORPORATE".equals(type)) {
                dto = new CorporateCustomerDto();
            } else if ("RETAIL".equals(type)) {
                dto = new RetailCustomerDto();
            }
        }
        return dto;
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
