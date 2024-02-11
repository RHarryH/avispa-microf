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

package com.avispa.microf.model.invoice.type.correction.service;

import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.content.ContentMapper;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.filestore.FileStore;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.microf.model.invoice.AbstractInvoiceService;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.service.file.IInvoiceFile;
import com.avispa.microf.model.invoice.service.file.data.InvoiceData;
import com.avispa.microf.model.invoice.service.file.data.InvoiceDataConverter;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceDto;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceMapper;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
@Slf4j
public class CorrectionInvoiceService extends AbstractInvoiceService<CorrectionInvoice, CorrectionInvoiceDto, CorrectionInvoiceRepository, CorrectionInvoiceMapper> {
    private final InvoiceDataConverter invoiceDataConverter;

    @Autowired
    public CorrectionInvoiceService(CorrectionInvoiceRepository repository, CorrectionInvoiceMapper invoiceMapper,
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
    protected InvoiceData getInvoiceData(CorrectionInvoice invoice) {
        return null; //TODO
    }

    @Override
    protected IInvoiceFile getInvoiceFile(String templatePath) {
        return null; //TODO
    }
}
