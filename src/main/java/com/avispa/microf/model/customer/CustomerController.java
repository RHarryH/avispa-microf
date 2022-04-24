package com.avispa.microf.model.customer;

import com.avispa.microf.model.base.BaseModalableController;
import com.avispa.microf.model.customer.exception.CustomerInUseException;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalMode;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/customer")
@Slf4j
public class CustomerController extends BaseModalableController<Customer, CustomerDto, CustomerMapper, CustomerService, CustomerModalContext> {
    public CustomerController(CustomerMapper customerMapper,
                              CustomerService customerService,
                              ModalService modalService) {
        super(customerService, customerMapper, modalService);
    }

    @Override
    public ModelAndView getAddModal() {
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id("customer-add-modal")
                .title("Add new customer")
                .action("/customer/modal/add")
                .size("large")
                .build();

        return getModal(modal);
    }

    @Override
    public ModelAndView getUpdateModal(UUID id) {
        Customer customer = getService().findById(id);
        CustomerDto customerDto = getEntityDtoMapper().convertToDto(customer);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id("customer-update-modal")
                .title("Update " + getDescription(customer) + " customer")
                .action("/customer/modal/update/" + id)
                .size("large")
                .build();

        return getModal(customerDto, modal);
    }

    private String getDescription(Customer customer) {
        return customer.getType().equals("CORPORATE") ? "corporate" : "retail";
    }

    @Override
    public void delete(UUID id) {
        try {
            super.delete(id);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Customer '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new CustomerInUseException(errorMessage);
        }
    }
}
