package com.avispa.microf.model.invoice.service.file;

import com.avispa.microf.model.invoice.service.file.data.InvoiceData;

import java.io.Closeable;
import java.nio.file.Path;

/**
 * @author Rafał Hiszpański
 */
public interface IInvoiceFile extends Closeable {

    void generate(InvoiceData invoiceData, String issuerName);

    Path save(String path);

    String getExtension();
}
