package com.avispa.microf.service.invoice.file;


import com.avispa.cms.model.content.Content;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.service.invoice.replacer.DocxReplacer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Rafał Hiszpański
 */
public class OfficeInvoiceFile extends AbstractInvoiceFile{
    private static final Logger log = LoggerFactory.getLogger(OfficeInvoiceFile.class);

    private XWPFDocument invoice;

    public OfficeInvoiceFile(Invoice invoice) {
        super(invoice);

        try {
            this.invoice = new XWPFDocument(ClassLoader.getSystemClassLoader().getResourceAsStream("vat_invoice_variables_template.docx"));
            this.replacer = new DocxReplacer(this.invoice);
        } catch (IOException e) {
            log.error("Unable to load document", e);
        }
    }

    @Override
    public Content save(String path) {
        Content content = getContent(path);
        try(FileOutputStream out = new FileOutputStream(content.getFileStorePath())) {
            invoice.write(out);

        } catch(IOException e) {
            log.error("Unable to save document", e);
        }

        return content;
    }

    @Override
    protected String getExtension() {
        return "docx";
    }

    @Override
    public void close() throws IOException {
        invoice.close();
    }
}
