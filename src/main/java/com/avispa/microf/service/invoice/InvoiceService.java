package com.avispa.microf.service.invoice;

import com.avispa.ecm.model.configuration.EcmConfigObject;
import com.avispa.ecm.model.configuration.autoname.Autoname;
import com.avispa.ecm.model.configuration.autoname.AutonameService;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.ecm.model.filestore.FileStore;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.microf.controller.InvoiceNotFoundException;
import com.avispa.microf.dto.AttachementDto;
import com.avispa.microf.dto.InvoiceDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceMapper;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.service.invoice.counter.CounterStrategy;
import com.avispa.microf.service.invoice.file.IInvoiceFile;
import com.avispa.microf.service.invoice.file.ODFInvoiceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.avispa.ecm.util.Formats.PDF;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final ContentService contentService;
    private final RenditionService renditionService;
    private final FileStore fileStore;
    private final CounterStrategy counterStrategy;

    private final AutonameService autonameService;
    private final ContextService contextService;

    @Transactional
    public void addInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);

        invoice.setSerialNumber(counterStrategy.getNextSerialNumber(invoice));

        List<EcmConfigObject> configObjectList = contextService.getMatchingConfigurations(invoice);
        configObjectList.stream().filter(e -> e instanceof Autoname).findFirst().ifPresent(a -> autonameService.apply((Autoname) a, invoice));

        generateInvoiceContent(invoice);
    }

    private void generateInvoiceContent(Invoice invoice) {
        try (IInvoiceFile invoiceFile = new ODFInvoiceFile(invoice)) {
            invoiceFile.generate();
            Path fileStorePath = invoiceFile.save(fileStore.getRootPath());
            Content content = contentService.createNewContent(invoiceFile.getExtension(), invoice, fileStorePath);

            renditionService.generate(content);
        } catch (FileNotFoundException e) {
            log.error("Cannot open template file", e);
        } catch (IOException e) {
            log.error("Exception during invoice generation", e);
        }
    }

    @Transactional
    public void updateInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = findById(invoiceDto.getId());
        invoiceMapper.updateInvoiceFromDto(invoiceDto, invoice);

        // delete old content and create new one
        contentService.deleteByDocument(invoice);
        generateInvoiceContent(invoice);
    }

    public void deleteInvoice(UUID id) {
        invoiceRepository.delete(findById(id));
    }

    public AttachementDto getRendition(UUID id) {
        Invoice invoice = findById(id);
        String renditionName = invoice.getObjectName().replace("/","_") + "." + PDF;

        Content content = contentService.findRenditionByDocumentId(id);

        return new AttachementDto(renditionName, content.getFileStorePath(), content.getSize());
    }

    public Invoice findById(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(InvoiceNotFoundException::new);
    }
}
