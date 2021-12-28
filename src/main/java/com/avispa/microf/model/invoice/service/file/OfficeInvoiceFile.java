package com.avispa.microf.model.invoice.service.file;

import com.avispa.microf.model.invoice.service.replacer.DocxReplacer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Deprecated(since="2.0.0")
public class OfficeInvoiceFile extends AbstractInvoiceFile{
    private static final Logger log = LoggerFactory.getLogger(OfficeInvoiceFile.class);

    private XWPFDocument invoice;

    public OfficeInvoiceFile() {
        try {
            this.invoice = new XWPFDocument(ClassLoader.getSystemClassLoader().getResourceAsStream("vat_invoice_variables_template.docx"));
            this.replacer = new DocxReplacer(this.invoice);
        } catch (IOException e) {
            log.error("Unable to load document", e);
        }
    }

    @Override
    public Path save(String path) {
        Path fileStorePath = Paths.get(path, UUID.randomUUID().toString());

        try(FileOutputStream out = new FileOutputStream(fileStorePath.toString())) {
            invoice.write(out);

        } catch(IOException e) {
            log.error("Unable to save document", e);
        }

        return fileStorePath;
    }

    @Override
    public String getExtension() {
        return "docx";
    }

    @Override
    public void close() throws IOException {
        invoice.close();
    }
}
