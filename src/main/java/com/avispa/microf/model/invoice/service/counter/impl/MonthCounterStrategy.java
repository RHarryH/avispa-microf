package com.avispa.microf.model.invoice.service.counter.impl;

import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.InvoiceRepository;
import com.avispa.microf.model.invoice.service.counter.CounterStrategy;
import lombok.RequiredArgsConstructor;

/**
 * This strategy resets counter every month based on the invoice service date
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public class MonthCounterStrategy implements CounterStrategy {
    private final InvoiceRepository invoiceRepository;

    @Override
    public int getNextSerialNumber(Invoice invoice) {
        return invoiceRepository.findMaxSerialNumberByMonth(invoice.getInvoiceDate().getMonthValue()) + 1;
    }
}
