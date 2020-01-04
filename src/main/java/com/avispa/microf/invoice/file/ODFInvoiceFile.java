package com.avispa.microf.invoice.file;


import com.avispa.microf.InputParameters;
import com.avispa.microf.invoice.replacer.OdtReplacer;
import com.avispa.microf.rendition.Rendition;
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

    public ODFInvoiceFile(InputParameters input) {
        super(input);

        try {
            this.invoice = OdfTextDocument.loadDocument(ClassLoader.getSystemClassLoader().getResourceAsStream("vat_invoice_variables_template.odt"));
            this.replacer = new OdtReplacer(invoice);
        } catch (Exception e) {
            log.error("Unable to load document", e);
        }
    }

    @Override
    public void save() {
        String inputPath = getInvoiceName() + "." + EXT;
        String renditionPath = getInvoiceName() + "." + Rendition.RENDITION_EXT;
        try(FileOutputStream out = new FileOutputStream(inputPath)) {
            invoice.save(out);
        } catch(Exception e) {
            log.error("Unable to save document");
        }

        new Rendition().generateRendition(inputPath, renditionPath);
    }

    @Override
    public void close() {
        invoice.close();
    }
}
