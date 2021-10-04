package com.avispa.microf.service.invoice.file;

import com.avispa.ecm.model.content.Content;

import java.io.Closeable;

/**
 * @author Rafał Hiszpański
 */
public interface IInvoiceFile extends Closeable {
    void generate();
    Content save(String path);
}
