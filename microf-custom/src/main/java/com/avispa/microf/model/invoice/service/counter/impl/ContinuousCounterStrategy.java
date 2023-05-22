package com.avispa.microf.model.invoice.service.counter.impl;

import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import lombok.RequiredArgsConstructor;

/**
 * This strategy gets last invoice number and appends one to it
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public class ContinuousCounterStrategy implements CounterStrategy {
    private final InvoiceRepository invoiceRepository;

    @Override
    public int getNextSerialNumber(Invoice invoice) {
        return invoiceRepository.findMaxSerialNumber() + 1;
    }
}
