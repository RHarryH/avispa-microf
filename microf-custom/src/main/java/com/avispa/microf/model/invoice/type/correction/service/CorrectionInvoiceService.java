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
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceDto;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceMapper;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoiceRepository;
import com.avispa.microf.model.invoice.type.correction.service.file.CorrectionInvoiceFile;
import com.avispa.microf.model.invoice.type.correction.service.file.data.CorrectionInvoiceData;
import com.avispa.microf.model.invoice.type.correction.service.file.data.CorrectionInvoiceDataConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
@Slf4j
public class CorrectionInvoiceService extends AbstractInvoiceService<CorrectionInvoice, CorrectionInvoiceDto, CorrectionInvoiceRepository, CorrectionInvoiceMapper> {
    private final CorrectionInvoiceDataConverter correctionInvoiceDataConverter;

    private final CorrectionInvoiceValidator validator;

    @Autowired
    public CorrectionInvoiceService(CorrectionInvoiceRepository repository, CorrectionInvoiceMapper correctionInvoiceMapper,
                                    CorrectionInvoiceDataConverter correctionInvoiceDataConverter,
                                    CorrectionInvoiceValidator validator,
                                    ContentService contentService,
                                    ContentMapper contentMapper,
                                    RenditionService renditionService,
                                    FileStore fileStore,
                                    CounterStrategy counterStrategy,
                                    ContextService contextService) {
        super(repository, correctionInvoiceMapper, contentService, contentMapper, renditionService, fileStore, counterStrategy, contextService);
        this.correctionInvoiceDataConverter = correctionInvoiceDataConverter;
        this.validator = validator;
    }

    @Override
    public void add(CorrectionInvoice entity) {
        validator.validate(entity);

        super.add(entity);
    }

    @Override
    protected CorrectionInvoiceData getInvoiceData(CorrectionInvoice invoice) {
        return correctionInvoiceDataConverter.convert(invoice);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected CorrectionInvoiceFile getInvoiceFile(String templatePath) {
        return new CorrectionInvoiceFile(templatePath);
    }
}
