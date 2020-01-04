package com.avispa.microf.invoice.file;

import java.awt.print.PrinterException;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author Rafał Hiszpański
 */
public interface IInvoiceFile extends Closeable {
    void generate();
    void save();
    void print() throws IOException, PrinterException;
}
