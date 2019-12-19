package com.avispa.microf.invoice;


import com.avispa.microf.InputParameters;
import com.avispa.microf.invoice.replacer.DocxReplacer;
import com.avispa.microf.invoice.replacer.ITemplateReplacer;
import com.avispa.microf.numeral.NumeralToStringConverter;
import com.avispa.microf.rendition.Rendition;
import com.avispa.microf.util.FormatUtils;
import com.avispa.microf.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class InvoiceFile implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(InvoiceFile.class);

    private InvoiceDAO dao;
    private XWPFDocument invoice;
    private ITemplateReplacer replacer;

    public InvoiceFile(InputParameters input) throws IOException {
        this.dao = new InvoiceDAO(input);
        this.invoice = new XWPFDocument(ClassLoader.getSystemClassLoader().getResourceAsStream("vat_invoice_variables_template.docx"));
        this.replacer = new DocxReplacer(invoice);
    }

    public void generate() {
        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", dao.getInvoiceNumber());
        variables.put("invoice_date", dao.getInvoiceDate());
        variables.put("service_date", dao.getServiceDate());
        variables.put("quantity", "1");
        variables.put("price_no_discount", dao.getValue());
        variables.put("price", dao.getValue());
        variables.put("value", dao.getValue());
        variables.put("vat", dao.getVat());
        variables.put("gross_value", dao.getGrossValue());
        variables.put("gross_value_in_words", dao.getGrossValueInWords());
        variables.put("payment_date", dao.getPaymentDate());
        variables.put("version", PropertiesUtils.getApplicationVersion());

        replacer.replaceVariables(variables);
    }

    public void save() throws IOException {
        String inputPath = getInvoiceName() + ".docx";
        String renditionPath = getInvoiceName() + "." + Rendition.RENDITION_EXT;
        try(FileOutputStream out = new FileOutputStream(inputPath)) {
            invoice.write(out);
        }

        new Rendition().generateRendition(inputPath, renditionPath);
    }

    public void print() throws IOException, PrinterException {
        String sourcePath = getInvoiceName() + "." + Rendition.RENDITION_EXT;

        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        PDDocument document = PDDocument.load(new File(sourcePath));

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(service);
        boolean doPrint = job.printDialog();

        if (doPrint) {
            try {
                job.print();
                log.info("Printing started");
            } catch (PrinterException e) {
                log.error("PrinterJob did not complete succesfully", e);
            }
        }

        //Desktop.getDesktop().print(new File(filePath));
    }

    @Override
    public void close() throws IOException {
        invoice.close();
    }

    private String getInvoiceName() {
        return dao.getInvoiceNumber().replace("/","_");
    }
}
