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

package com.avispa.microf.model.invoice.type.vat.service;

import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.content.ContentMapper;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.filestore.FileStore;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.microf.model.invoice.AbstractInvoiceService;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import com.avispa.microf.model.invoice.type.vat.InvoiceDto;
import com.avispa.microf.model.invoice.type.vat.InvoiceMapper;
import com.avispa.microf.model.invoice.type.vat.InvoiceRepository;
import com.avispa.microf.model.invoice.type.vat.service.file.InvoiceFile;
import com.avispa.microf.model.invoice.type.vat.service.file.data.InvoiceData;
import com.avispa.microf.model.invoice.type.vat.service.file.data.InvoiceDataConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
@Slf4j
public class InvoiceService extends AbstractInvoiceService<Invoice, InvoiceDto, InvoiceRepository, InvoiceMapper> {
    private final InvoiceDataConverter invoiceDataConverter;

    @Autowired
    public InvoiceService(InvoiceRepository repository, InvoiceMapper invoiceMapper,
                          InvoiceDataConverter invoiceDataConverter,
                          ContentService contentService,
                          ContentMapper contentMapper,
                          RenditionService renditionService,
                          FileStore fileStore,
                          CounterStrategy counterStrategy,
                          ContextService contextService) {
        super(repository, invoiceMapper, contentService, contentMapper, renditionService, fileStore, counterStrategy, contextService);
        this.invoiceDataConverter = invoiceDataConverter;
    }

    @Override
    protected InvoiceData getInvoiceData(Invoice invoice) {
        return invoiceDataConverter.convert(invoice);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected InvoiceFile getInvoiceFile(String templatePath) {
        return new InvoiceFile(templatePath);
    }
}
