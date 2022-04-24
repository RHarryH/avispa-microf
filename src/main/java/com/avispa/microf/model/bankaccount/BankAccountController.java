package com.avispa.microf.model.bankaccount;

import com.avispa.microf.model.bankaccount.exception.BankAccountInUseException;
import com.avispa.microf.model.base.BaseModalableController;
import com.avispa.microf.model.ui.modal.ModalConfiguration;
import com.avispa.microf.model.ui.modal.ModalMode;
import com.avispa.microf.model.ui.modal.ModalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Controller
@RequestMapping("/bank/account")
@Slf4j
public class BankAccountController extends BaseModalableController<BankAccount, BankAccountDto, BankAccountMapper, BankAccountService, BankAccountModalContext> {
    public BankAccountController(BankAccountMapper customerMapper,
                                 BankAccountService customerService,
                                 ModalService modalService) {
        super(customerService, customerMapper, modalService);
    }

    @Override
    public ModelAndView getAddModal() {
        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.INSERT)
                .id("bank-account-add-modal")
                .title("Add new bank account")
                .action("/bank/account/modal/add")
                .size("large")
                .build();

        return getModal(modal);
    }

    @Override
    public ModelAndView getUpdateModal(UUID id) {
        BankAccount bankAccount = getService().findById(id);
        BankAccountDto bankAccountDto = getEntityDtoMapper().convertToDto(bankAccount);

        ModalConfiguration modal = ModalConfiguration.builder(ModalMode.UPDATE)
                .id("bank-account-update-modal")
                .title("Update bank account")
                .action("/bank/account/modal/update/" + id)
                .size("large")
                .build();

        return getModal(bankAccountDto, modal);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") UUID id) {
        try {
            super.delete(id);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Bank account '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new BankAccountInUseException(errorMessage);
        }
    }
}
