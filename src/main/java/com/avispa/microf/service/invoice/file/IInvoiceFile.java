package com.avispa.microf.service.invoice.file;

import org.springframework.scheduling.annotation.Async;

import java.awt.print.PrinterException;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author Rafał Hiszpański
 */
public interface IInvoiceFile extends Closeable {
    @Async
    void generate();
    void save();
    void print() throws IOException, PrinterException;
}
