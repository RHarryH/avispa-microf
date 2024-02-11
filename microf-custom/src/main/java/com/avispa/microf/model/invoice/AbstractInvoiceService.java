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

package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.base.BaseEcmService;
import com.avispa.ecm.model.configuration.callable.autolink.Autolink;
import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.template.Template;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.content.ContentDto;
import com.avispa.ecm.model.content.ContentMapper;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.filestore.FileStore;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.ecm.util.exception.EcmException;
import com.avispa.ecm.util.exception.ResourceNotFoundException;
import com.avispa.ecm.util.transaction.TransactionUtils;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.service.file.IInvoiceFile;
import com.avispa.microf.model.invoice.service.file.data.InvoiceData;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public abstract class AbstractInvoiceService<I extends BaseInvoice, D extends BaseInvoiceDto, R extends EcmObjectRepository<I>, M extends AbstractInvoiceMapper<I, D>> extends BaseEcmService<I, D, R, M> {
    private final ContentService contentService;
    private final ContentMapper contentMapper;

    private final RenditionService renditionService;
    private final FileStore fileStore;
    private final CounterStrategy counterStrategy;

    private final ContextService contextService;

    @Value("${microf.issuer-name:John Doe}")
    private String issuerName;

    protected AbstractInvoiceService(R repository, M invoiceMapper,
                                     ContentService contentService,
                                     ContentMapper contentMapper,
                                     RenditionService renditionService,
                                     FileStore fileStore,
                                     CounterStrategy counterStrategy,
                                     ContextService contextService) {
        super(repository, invoiceMapper);
        this.contentService = contentService;
        this.contentMapper = contentMapper;
        this.renditionService = renditionService;
        this.fileStore = fileStore;
        this.counterStrategy = counterStrategy;
        this.contextService = contextService;
    }

    @Override
    protected void add(I invoice) {
        invoice.setSerialNumber(counterStrategy.getNextSerialNumber(invoice));

        contextService.applyMatchingConfigurations(invoice, List.of(Autoname.class, Autolink.class));

        generateContent(invoice);
    }

    @Override
    protected void update(I invoice) {
        // delete old content and create new one
        contentService.deleteByRelatedEntity(invoice);
        generateContent(invoice);
    }

    private void generateContent(I invoice) {
        InvoiceData invoiceData = getInvoiceData(invoice);

        Template template = contextService.getConfiguration(invoice, Template.class).
                orElseThrow(() -> new EcmException("Document template for '" + invoice.getId() + "' document couldn't be found"));
        Content templateContent = template.getPrimaryContent();

        if(null == templateContent) {
            throw new EcmException("Template " + template.getObjectName() + " does not have any content file assigned");
        }

        try (IInvoiceFile invoiceFile = getInvoiceFile(templateContent.getFileStorePath())) {
            invoiceFile.generate(invoiceData, issuerName);

            // save file in the file store and create content object
            Path fileStorePath = invoiceFile.save(fileStore.getRootPath());
            TransactionUtils.registerFileRollback(fileStorePath);
            Content content = contentService.createNewContent(invoiceFile.getExtension(), invoice, fileStorePath);

            renditionService.generate(content);
        } catch (FileNotFoundException e) {
            log.error("Cannot open template file", e);
            throw new EcmException("Cannot open template file");
        } catch (IOException e) {
            log.error("IO during invoice generation", e);
            throw new EcmException("IO during invoice generation");
        }
    }

    public ContentDto getRendition(UUID id) {
        return contentMapper.convertToDto(contentService.findPdfRenditionByDocumentId(id));
    }

    @Override
    public I findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Invoice.class));
    }

    protected abstract InvoiceData getInvoiceData(I invoice);

    protected abstract IInvoiceFile getInvoiceFile(String templatePath);
}
