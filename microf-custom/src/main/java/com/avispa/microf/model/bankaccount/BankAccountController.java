/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.model.bankaccount;

import com.avispa.ecm.model.base.controller.BaseEcmController;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/bank-account")
@Tag(name = "Bank account", description = "Management of bank accounts - insertion, update and deletion")
@Slf4j
public class BankAccountController extends BaseEcmController<BankAccount, BankAccountDto, BankAccountService> {

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        super(bankAccountService);
    }

    @Override
    @ApiResponse(responseCode = "400", description = "Bank account cannot be deleted because it is in use in invoices", content = @Content)
    public void delete(UUID id, String resourceName) {
        try {
            super.delete(id, resourceName);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Bank account '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }
    }
}
