package com.avispa.microf.model.invoice.service;

import com.avispa.ecm.model.configuration.callable.autolink.Autolink;
import com.avispa.ecm.model.configuration.callable.autoname.Autoname;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.ecm.model.filestore.FileStore;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.content.ContentDto;
import com.avispa.microf.model.content.ContentMapper;
import com.avispa.microf.model.error.ResourceNotFoundException;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.invoice.InvoiceMapper;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import com.avispa.microf.model.invoice.service.file.IInvoiceFile;
import com.avispa.microf.model.invoice.service.file.OdfInvoiceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceService implements BaseService<Invoice, InvoiceDto> {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    private final ContentService contentService;
    private final ContentMapper contentMapper;

    private final RenditionService renditionService;
    private final FileStore fileStore;
    private final CounterStrategy counterStrategy;

    private final ContextService contextService;

    @Value("${microf.issuer-name:John Doe}")
    private String issuerName;

    @Transactional
    @Override
    public void add(Invoice invoice) {
        invoiceRepository.save(invoice);

        invoice.setSerialNumber(counterStrategy.getNextSerialNumber(invoice));

        contextService.applyMatchingConfigurations(invoice, Autoname.class, Autolink.class);

        generateContent(invoice);
    }

    @Transactional
    @Override
    public void update(InvoiceDto invoiceDto) {
        Invoice invoice = findById(invoiceDto.getId());
        invoiceMapper.updateEntityFromDto(invoiceDto, invoice);

        // delete old content and create new one
        contentService.deleteByRelatedObject(invoice);
        generateContent(invoice);
    }

    private void generateContent(Invoice invoice) {
        try (IInvoiceFile invoiceFile = new OdfInvoiceFile()) {
            invoiceFile.generate(invoice, issuerName);
            Path fileStorePath = invoiceFile.save(fileStore.getRootPath());
            Content content = contentService.createNewContent(invoiceFile.getExtension(), invoice, fileStorePath);

            renditionService.generate(content);
        } catch (FileNotFoundException e) {
            log.error("Cannot open template file", e);
        } catch (IOException e) {
            log.error("Exception during invoice generation", e);
        }
    }

    @Override
    public void delete(UUID id) {
        invoiceRepository.delete(findById(id));
    }

    public ContentDto getRendition(UUID id) {
        return contentMapper.convertToDto(contentService.findPdfRenditionByDocumentId(id));
    }

    @Override
    public Invoice findById(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Invoice.class));
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }
}
