package com.avispa.microf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class InputParameters {
    private static final String SERIAL_NUMBER = "serial_number";
    private static final String INVOICE_DATE = "invoice_date";
    private static final String SERVICE_DATE = "service_date";
    private static final String VALUE = "value";

    private Map<String, Object> parameters = new HashMap<>();

    public void setSerialNumber(Integer serial) {
        parameters.put(SERIAL_NUMBER, serial);
    }

    public Integer getSerialNumber() {
        return (Integer) parameters.get(SERIAL_NUMBER);
    }

    public void setInvoiceDate(LocalDate date) {
        parameters.put(INVOICE_DATE, date);
    }

    public LocalDate getInvoiceDate() {
        return (LocalDate) parameters.get(INVOICE_DATE);
    }

    public void setServiceDate(LocalDate date) {
        parameters.put(SERVICE_DATE, date);
    }

    public LocalDate getServiceDate() {
        return (LocalDate) parameters.get(SERVICE_DATE);
    }

    public void setValue(BigDecimal value) {
        parameters.put(VALUE, value);
    }

    public BigDecimal getValue() {
        return (BigDecimal) parameters.get(VALUE);
    }
}
