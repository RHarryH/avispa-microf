package com.avispa.microf.service.invoice.file;

import java.io.Closeable;
import java.nio.file.Path;

/**
 * @author Rafał Hiszpański
 */
public interface IInvoiceFile extends Closeable {

    void generate();

    Path save(String path);

    String getExtension();
}
