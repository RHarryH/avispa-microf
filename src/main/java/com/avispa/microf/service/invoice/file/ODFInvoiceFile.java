package com.avispa.microf.service.invoice.file;


import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.service.invoice.replacer.OdtReplacer;
import com.avispa.microf.service.rendition.RenditionService;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;

/**
 * @author Rafał Hiszpański
 */
public class ODFInvoiceFile extends AbstractInvoiceFile {
    private static final Logger log = LoggerFactory.getLogger(ODFInvoiceFile.class);
    private static final String EXT = "odt";

    private OdfTextDocument invoice;

    public ODFInvoiceFile(Invoice invoice) {
        super(invoice);

        try {
            this.invoice = OdfTextDocument.loadDocument(ClassLoader.getSystemClassLoader().getResourceAsStream("vat_invoice_variables_template.odt"));
            this.replacer = new OdtReplacer(this.invoice);
        } catch (Exception e) {
            log.error("Unable to load document", e);
        }
    }

    @Override
    public void save() {
        String inputPath = getInvoiceName() + "." + EXT;
        String renditionPath = getInvoiceName() + "." + RenditionService.RENDITION_EXT;
        try(FileOutputStream out = new FileOutputStream(inputPath)) {
            invoice.save(out);
        } catch(Exception e) {
            log.error("Unable to save document");
        }

        new RenditionService().generate(inputPath, renditionPath);
    }

    @Override
    public void close() {
        invoice.close();
    }
}
