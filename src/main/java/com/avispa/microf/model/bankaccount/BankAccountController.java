package com.avispa.microf.model.bankaccount;

import com.avispa.microf.model.bankaccount.exception.BankAccountInUseException;
import com.avispa.microf.model.base.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Controller
@RequestMapping("/bank-account")
@Slf4j
public class BankAccountController extends BaseController<BankAccount, BankAccountDto, BankAccountService> {

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        super(bankAccountService);
    }

    @Override
    protected BankAccountDto createDto(Map<String, Object> object) {
        return new BankAccountDto();
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
