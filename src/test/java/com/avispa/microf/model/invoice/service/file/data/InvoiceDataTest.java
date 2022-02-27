package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.ecm.model.configuration.dictionary.DictionaryValue;
import com.avispa.microf.model.customer.Address;
import com.avispa.microf.model.customer.type.retail.RetailCustomer;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.position.Position;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
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
        position.setQuantity(BigDecimal.ONE);
        position.setUnit(new DictionaryValue());

        DictionaryValue vatRateDictionaryValue = spy(DictionaryValue.class);
        vatRateDictionaryValue.setKey("Key");
        vatRateDictionaryValue.setLabel("Label");

        Dictionary dictionary = mock(Dictionary.class);
        doReturn(dictionary).when(vatRateDictionaryValue).getDictionary();
        doReturn("5.00").when(dictionary).getColumnValue(anyString(), eq("rate"));
        position.setVatRate(vatRateDictionaryValue);

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

        InvoiceData invoiceData = new InvoiceData(invoice);

        ObjectMapper oMapper = new ObjectMapper();

        TypeReference<LinkedHashMap<String, Object>> typeRef = new TypeReference<>() {};
        Map<String, Object> map = oMapper.convertValue(invoiceData, typeRef);

        log.info(map.toString());

        assertEquals("123", map.get("invoiceName"));
        assertEquals("Position", ((Map<?, ?>)((List<?>)map.get("positions")).get(0)).get("positionName"));
    }
}