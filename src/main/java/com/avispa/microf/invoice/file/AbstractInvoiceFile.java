package com.avispa.microf.invoice.file;

import com.avispa.microf.InputParameters;
import com.avispa.microf.invoice.InvoiceDAO;
import com.avispa.microf.invoice.replacer.ITemplateReplacer;
import com.avispa.microf.rendition.Rendition;
import com.avispa.microf.util.InternalConfiguration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractInvoiceFile implements IInvoiceFile {
    private static final Logger log = LoggerFactory.getLogger(AbstractInvoiceFile.class);

    private InvoiceDAO dao;
    protected ITemplateReplacer replacer;

    public AbstractInvoiceFile(InputParameters input) {
        this.dao = new InvoiceDAO(input);
    }

    @Override
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
        variables.put("version", InternalConfiguration.getReleaseApplicationVersion());

        replacer.replaceVariables(variables);
    }

    @Override
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
    }

    protected String getInvoiceName() {
        return dao.getInvoiceNumber().replace("/","_");
    }
}
