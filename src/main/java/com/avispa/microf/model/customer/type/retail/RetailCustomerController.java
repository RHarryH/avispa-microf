package com.avispa.microf.model.customer.type.retail;

import com.avispa.microf.model.base.BaseModalableController;
import com.avispa.microf.model.customer.exception.CustomerInUseException;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalMode;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/customer/retail")
@Slf4j
public class RetailCustomerController extends BaseModalableController<RetailCustomer, RetailCustomerDto, RetailCustomerMapper, RetailCustomerService, RetailCustomerModalContext> {
    @Autowired
    public RetailCustomerController(RetailCustomerMapper customerMapper,
                                    RetailCustomerService customerService,
                                    ModalService modalService) {
        super(customerService, customerMapper, modalService);
    }

    @Override
    public ModelAndView getAddModal() {
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id("customer-retail-add-modal")
                .title("Add new retail customer")
                .action("/customer/retail/modal/add")
                .size("large")
                .build();

        return getModal(RetailCustomer.class, RetailCustomerDto.class, modal);
    }

    @Override
    public ModelAndView getUpdateModal(UUID id) {
        RetailCustomer customer = getService().findById(id);
        RetailCustomerDto customerDto = getEntityDtoMapper().convertToDto(customer);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id("customer-retail-update-modal")
                .title("Update retail customer")
                .action("/customer/retail/modal/update/" + id)
                .size("large")
                .build();

        return getModal(RetailCustomer.class, customerDto, modal);
    }

    @Override
    public void delete(UUID id) {
        try {
            super.delete(id);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Retail customer '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new CustomerInUseException(errorMessage);
        }
    }
}
