package com.avispa.microf.service.invoice.file;

import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.service.invoice.replacer.ITemplateReplacer;
import com.avispa.microf.service.rendition.RenditionService;
import com.avispa.microf.util.InternalConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractInvoiceFile implements IInvoiceFile {
    private static final Logger log = LoggerFactory.getLogger(AbstractInvoiceFile.class);

    private final Invoice invoice;
    protected ITemplateReplacer replacer;

    protected AbstractInvoiceFile(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public void generate() {
        Map<String, String> variables = new HashMap<>();
        variables.put("invoice_number", invoice.getInvoiceNumber());
        variables.put("invoice_date", invoice.getInvoiceDateAsString());
        variables.put("service_date", invoice.getServiceDateAsString());
        variables.put("quantity", "1");
        variables.put("price_no_discount", invoice.getNetValueAsString());
        variables.put("price", invoice.getNetValueAsString());
        variables.put("net_value", invoice.getNetValueAsString());
        variables.put("vat", invoice.getVatAsString());
        variables.put("gross_value", invoice.getGrossValueAsString());
        variables.put("gross_value_in_words", invoice.getGrossValueInWords());
        variables.put("payment_date", invoice.getPaymentDateAsString());
        variables.put("comments", invoice.getComments());
        variables.put("version", InternalConfiguration.getReleaseApplicationVersion());

        replacer.replaceVariables(variables);
    }

    @Override
    public void print() throws IOException, PrinterException {
        String sourcePath = getInvoiceName() + "." + RenditionService.RENDITION_EXT;

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
        return invoice.getInvoiceNumber().replace("/","_");
    }

    @Override
    public byte[] asByteArray() throws IOException {
        InputStream in = new FileInputStream(getInvoiceName() + "." + RenditionService.RENDITION_EXT);
        return IOUtils.toByteArray(in);
    }
}
