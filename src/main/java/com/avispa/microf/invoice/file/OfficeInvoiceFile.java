package com.avispa.microf.invoice.file;


import com.avispa.microf.InputParameters;
import com.avispa.microf.invoice.replacer.DocxReplacer;
import com.avispa.microf.rendition.Rendition;
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
    private static final String EXT = "docx";

    private XWPFDocument invoice;

    public OfficeInvoiceFile(InputParameters input) {
        super(input);

        try {
            this.invoice = new XWPFDocument(ClassLoader.getSystemClassLoader().getResourceAsStream("vat_invoice_variables_template.docx"));
            this.replacer = new DocxReplacer(invoice);
        } catch (IOException e) {
            log.error("Unable to load document", e);
        }
    }

    @Override
    public void save() {
        String inputPath = getInvoiceName() + "." + EXT;
        String renditionPath = getInvoiceName() + "." + Rendition.RENDITION_EXT;
        try(FileOutputStream out = new FileOutputStream(inputPath)) {
            invoice.write(out);
        } catch(IOException e) {
            log.error("Unable to save document");
        }

        new Rendition().generateRendition(inputPath, renditionPath);
    }

    @Override
    public void close() throws IOException {
        invoice.close();
    }


}
