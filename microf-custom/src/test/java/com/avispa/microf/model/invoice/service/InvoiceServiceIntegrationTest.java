/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.model.invoice.service;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.template.Template;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.error.ResourceNotFoundException;
import com.avispa.ecm.model.format.Format;
import com.avispa.ecm.model.format.FormatRepository;
import com.avispa.ecm.service.rendition.RenditionService;
import com.avispa.ecm.util.exception.EcmException;
import com.avispa.microf.model.bankaccount.BankAccount;
import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.position.Position;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest(properties = {
        "jodconverter.local.enabled=false",
        "avispa.ecm.configuration.paths=src/test/resources/config/microf-test-configuration.zip"
})
@ActiveProfiles("test")
class InvoiceServiceIntegrationTest {
    private static final String fileStorePath = Path.of("target", "rendition-test").toAbsolutePath().toString();

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("avispa.ecm.file-store.path", () -> fileStorePath);
    }

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private EcmObjectRepository<EcmObject> ecmObjectRepository;

    @Autowired
    private FormatRepository formatRepository;

    @MockBean
    private ContextService contextService;

    @MockBean
    private RenditionService renditionService;

    @TestConfiguration
    static class InvoiceServiceIntegrationConfig {
        // overwrite default executor to force async methods to be run sync
        @Bean
        public Executor taskExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @BeforeEach
    public void mockRenditionService() {
        when(renditionService.generate(any())).thenAnswer(answer -> CompletableFuture.completedFuture(answer.getArgument(0)));
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(new File(fileStorePath));
    }

    @Test
    void givenInvoice_whenAdded_thenContentsAreInDBAndFileStore() {
        int filesCount = getCount();
        Invoice invoice = createInvoice();
        Template template = createValidTemplate();
        mockContextService(template);

        invoiceService.add(invoice);

        assertNotNull(invoiceService.findById(invoice.getId()));
        for(Content content : invoice.getContents()) {
            assertNotNull(ecmObjectRepository.findById(content.getId()));
        }
        assertEquals(filesCount + invoice.getContents().size(), getCount());
    }

    @Test
    void givenCorruptedDB_whenAdded_thenBothOdtAndPdfDoesNotExist() {
        int filesCount = getCount();
        Invoice invoice = createInvoice();
        Template template = createCorruptedTemplate();
        mockContextService(template);

        assertThrows(EcmException.class, () -> invoiceService.add(invoice));
        UUID invoiceId = invoice.getId();
        assertThrows(ResourceNotFoundException.class, () -> invoiceService.findById(invoiceId));
        assertNull(invoice.getContents());
        assertEquals(filesCount, getCount());
    }

    private Invoice createInvoice() {
        Customer customer = new Customer();
        customer.setType("CORPORATE");
        ecmObjectRepository.save(customer);

        Position position = new Position();
        position.setPositionName("Position");
        position.setQuantity(BigDecimal.ONE);
        position.setUnit("HOUR");
        position.setVatRate("VAT_05");
        position.setUnitPrice(BigDecimal.TEN);
        position.setDiscount(BigDecimal.ZERO);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(UUID.randomUUID());
        bankAccount.setAccountNumber(RandomStringUtils.randomAlphanumeric(24));
        bankAccount.setBankName("test bank");
        ecmObjectRepository.save(bankAccount);

        Payment payment = new Payment();
        payment.setMethod("BANK_TRANSFER");
        payment.setDeadline(14);
        payment.setPaidAmount(BigDecimal.ZERO);
        payment.setBankAccount(bankAccount);

        Invoice invoice = new Invoice();
        invoice.setObjectName("Test");
        invoice.setIssueDate(LocalDate.now());
        invoice.setServiceDate(LocalDate.now());
        invoice.setBuyer(customer);
        invoice.setSeller(customer);
        invoice.setPositions(List.of(position));
        invoice.setPayment(payment);

        return invoice;
    }

    private int getCount() {
        String[] files = new File(fileStorePath).list();
        return null != files ? files.length : 0;
    }

    private void mockContextService(Template template) {
        when(contextService.getConfiguration(any(Invoice.class), eq(Template.class))).thenReturn(Optional.of(template));
        doNothing().when(contextService)
                .applyMatchingConfigurations(any(Invoice.class), anyList());
    }

    private static Template createCorruptedTemplate() {
        Template template = new Template();
        template.setObjectName("Test");
        return template;
    }

    private Template createValidTemplate() {
        Format format = formatRepository.findByExtension("odt");

        Content content = new Content();
        content.setFileStorePath(Path.of("src/test/resources/test.odt").toAbsolutePath().toString());
        content.setFormat(format);

        Template template = new Template();
        template.setObjectName("Test");
        template.setContents(Set.of(content));
        return template;
    }
}