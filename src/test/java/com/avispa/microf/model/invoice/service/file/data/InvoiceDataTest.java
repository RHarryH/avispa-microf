package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.customer.address.Address;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.position.Position;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class InvoiceDataTest {
    @Mock
    private Dictionary unitDict;

    @Mock
    private Dictionary vatRateDict;

    @Test
    void convertToDetails() {
        when(unitDict.isEmpty()).thenReturn(false);
        when(unitDict.getLabel(any(String.class))).thenReturn("godz.");

        when(vatRateDict.getLabel(any(String.class))).thenReturn("5%");
        when(vatRateDict.getColumnValue(any(String.class), any(String.class))).thenReturn("0.05");

        Address address = new Address();
        address.setObjectName("A");
        address.setPlace("Kielce");
        address.setStreet("Strit");
        address.setZipCode("123");

        Customer customer = new Customer();
        customer.setType("RETAIL");
        customer.setObjectName("Name Second name");
        customer.setFirstName("Name");
        customer.setLastName("Second name");
        customer.setEmail("a@a.pl");
        customer.setPhoneNumber("+48 123 123 123");
        customer.setAddress(address);

        Position position = new Position();
        position.setPositionName("Position");
        position.setQuantity(BigDecimal.ONE);
        position.setUnit("HOUR");
        position.setVatRate("VAT_05");

        position.setUnitPrice(BigDecimal.TEN);
        position.setDiscount(BigDecimal.ZERO);

        Invoice invoice = new Invoice();
        invoice.setObjectName("123");
        invoice.setBuyer(customer);
        invoice.setSeller(customer);
        invoice.setIssueDate(LocalDate.now());
        invoice.setServiceDate(LocalDate.now());
        invoice.setComments("Comment");
        invoice.setPositions(List.of(position));

        InvoiceData invoiceData = new InvoiceData(invoice, unitDict, vatRateDict);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        TypeReference<LinkedHashMap<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> map = objectMapper.convertValue(invoiceData, typeRef);

        log.info(map.toString());

        assertEquals("123", map.get("invoiceName"));
        assertEquals("Position", ((Map<?, ?>)((List<?>)map.get("positions")).get(0)).get("positionName"));
    }
}