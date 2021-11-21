package com.avispa.microf.model.invoice.service.counter;

import com.avispa.microf.model.invoice.Invoice;

/**
 * Handles invoice serial number auto incrementation
 * @author Rafał Hiszpański
 */
public interface CounterStrategy {
    int getNextSerialNumber(Invoice invoice);
}
