/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice.type.vat.controller;

import com.avispa.microf.model.invoice.AbstractInvoiceController;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import com.avispa.microf.model.invoice.type.vat.InvoiceDto;
import com.avispa.microf.model.invoice.type.vat.service.InvoiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@RestController
@RequestMapping("/v1/invoice")
@Tag(name = "Invoice", description = "Management of invoices - insertion, update, deletion and rendition generating")
public class InvoiceController extends AbstractInvoiceController<Invoice, InvoiceDto, InvoiceService> {
    public InvoiceController(InvoiceService service) {
        super(service);
    }
}
