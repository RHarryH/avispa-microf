package com.avispa.microf.service.invoice.counter;

import com.avispa.microf.model.invoice.Invoice;

/**
 * Handles invoice serial number auto incrementation
 * @author Rafał Hiszpański
 */
public interface CounterStrategy {
    int getNextSerialNumber(Invoice invoice);
}
