package com.avispa.microf.model.customer.type.corporate;

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
@RequestMapping("/customer/corporate")
@Slf4j
public class CorporateCustomerController extends BaseModalableController<CorporateCustomer, CorporateCustomerDto, CorporateCustomerMapper, CorporateCustomerService, CorporateCustomerModalContext> {
    public CorporateCustomerController(CorporateCustomerMapper customerMapper,
                                       CorporateCustomerService customerService,
                                       ModalService modalService) {
        super(customerService, customerMapper, modalService);
    }

    @Override
    public ModelAndView getAddModal() {
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id("customer-corporate-add-modal")
                .title("Add new corporate customer")
                .action("/customer/corporate/modal/add")
                .size("large")
                .build();

        return getModal(CorporateCustomer.class, CorporateCustomerDto.class, modal);
    }

    @Override
    public ModelAndView getUpdateModal(UUID id) {
        CorporateCustomer customer = getService().findById(id);
        CorporateCustomerDto customerDto = getEntityDtoMapper().convertToDto(customer);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id("customer-corporate-update-modal")
                .title("Update corporate customer")
                .action("/customer/corporate/modal/update/" + id)
                .size("large")
                .build();

        return getModal(CorporateCustomer.class, customerDto, modal);
    }

    @Override
    public void delete(UUID id) {
        try {
            super.delete(id);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Corporate customer '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new CustomerInUseException(errorMessage);
        }
    }
}
