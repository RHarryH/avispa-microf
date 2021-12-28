package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.microf.constants.Unit;
import com.avispa.microf.constants.VatRate;
import com.avispa.microf.model.customer.Address;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.position.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
class InvoiceDataTest {
    @Test
    void convertToDetails() {
        Address address = new Address();
        address.setObjectName("A");
        address.setPlace("Kielce");
        address.setStreet("Strit");
        address.setZipCode("123");

        RetailCustomer customer = new RetailCustomer();
        customer.setObjectName("Name Second name");
        customer.setFirstName("Name");
        customer.setLastName("Second name");
        customer.setEmail("a@a.pl");
        customer.setPhoneNumber("+48 123 123 123");
        customer.setAddress(address);

        Position position = new Position();
        position.setPositionName("Position");
        position.setAmount(BigDecimal.ONE);
        position.setUnit(Unit.HOUR);
        position.setVatRate(VatRate.VAT_05);
        position.setUnitPrice(BigDecimal.TEN);
        position.setDiscount(BigDecimal.ZERO);

        Invoice invoice = new Invoice();
        invoice.setObjectName("123");
        invoice.setBuyer(customer);
        invoice.setSeller(customer);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setServiceDate(LocalDate.now());
        invoice.setComments("Comment");
        invoice.setPositions(List.of(position));

        InvoiceData invoiceData = new InvoiceData(invoice);

        ObjectMapper oMapper = new ObjectMapper();

       /* TypeReference<HashMap<String, String>> typeRef
                = new TypeReference<>() {};*/
        Map<String, String> map = oMapper.convertValue(invoiceData, Map.class);
        log.info(map.toString());
    }
}