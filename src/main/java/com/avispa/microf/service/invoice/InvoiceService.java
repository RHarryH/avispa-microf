package com.avispa.microf.service.invoice;

import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.content.ContentRepository;
import com.avispa.ecm.model.filestore.FileStore;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.microf.controller.InvoiceNotFoundException;
import com.avispa.microf.dto.AttachementDto;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.service.invoice.counter.CounterStrategy;
import com.avispa.microf.service.invoice.file.IInvoiceFile;
import com.avispa.microf.service.invoice.file.ODFInvoiceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.avispa.ecm.util.Formats.PDF;

/**
 * @author Rafał Hiszpański
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ContentRepository contentRepository;
    private final RenditionService renditionService;
    private final FileStore fileStore;
    private final CounterStrategy counterStrategy;

    public void addInvoice(Invoice invoice) {
        invoice.setSerialNumber(counterStrategy.getNextSerialNumber(invoice));
        try (IInvoiceFile invoiceFile = new ODFInvoiceFile(invoice)) {
            invoiceFile.generate();
            Content content = invoiceFile.save(fileStore.getRootPath());
            content.setDocument(invoice);
            invoiceRepository.save(invoice);

            renditionService.generate(content);
        } catch (FileNotFoundException e) {
            log.error("Cannot open template file", e);
        } catch (IOException e) {
            log.error("Exception during invoice generation", e);
        }
    }

    public void updateInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.delete(findById(id));
    }

    public AttachementDto getRendition(Long id) {
        Invoice invoice = findById(id);
        String renditionName = invoice.getObjectName().replace("/","_") + "." + PDF;

        Content content = contentRepository.findByDocumentIdAndExtension(id, PDF);

        return new AttachementDto(renditionName, content.getFileStorePath(), content.getSize());
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(InvoiceNotFoundException::new);
    }
}
