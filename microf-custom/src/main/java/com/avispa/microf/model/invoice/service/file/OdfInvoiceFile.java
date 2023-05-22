package com.avispa.microf.model.invoice.service.file;

import com.avispa.microf.model.invoice.service.replacer.InvoiceOdtReplacer;
import com.avispa.ecm.util.exception.EcmException;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public class OdfInvoiceFile extends AbstractInvoiceFile {
    private static final Logger log = LoggerFactory.getLogger(OdfInvoiceFile.class);

    private final OdfTextDocument invoice;

    public OdfInvoiceFile(String templatePath) {
        try {
            this.invoice = OdfTextDocument.loadDocument(new File(templatePath));
            this.replacer = new InvoiceOdtReplacer(this.invoice);
        } catch (Exception e) {
            log.error("Unable to load document", e);
            throw new EcmException("Unable to load document");
        }
    }

    @Override
    public Path save(String path) {
        Path fileStorePath = Paths.get(path, UUID.randomUUID().toString());

        try(FileOutputStream out = new FileOutputStream(fileStorePath.toString())) {
            invoice.save(out);
        } catch(Exception e) {
            log.error("Unable to save document", e);
        }

        return fileStorePath;
    }

    @Override
    public String getExtension() {
        return "odt";
    }

    @Override
    public void close() {
        if(null != invoice) {
            invoice.close();
        }
    }
}
