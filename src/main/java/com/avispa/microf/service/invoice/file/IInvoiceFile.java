package com.avispa.microf.service.invoice.file;

import java.io.Closeable;

/**
 * @author Rafał Hiszpański
 */
public interface IInvoiceFile extends Closeable {
    void generate();
    void save();

    String getInputPath();
}
