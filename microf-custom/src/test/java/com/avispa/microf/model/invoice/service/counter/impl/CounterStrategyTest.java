/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.microf.model.invoice.service.counter.impl;

import com.avispa.microf.model.customer.Customer;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.Month;

import static com.avispa.microf.util.InvoiceUtils.getCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@DataJpaTest
@Import({MonthCounterStrategy.class, ContinuousCounterStrategy.class})
class CounterStrategyTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MonthCounterStrategy monthCounterStrategy;

    @Autowired
    private ContinuousCounterStrategy continuousCounterStrategy;

    @BeforeEach
    void init() {
        Customer customer = getCustomer();
        entityManager.persist(customer);

        createInvoice(LocalDate.of(2024, Month.MARCH, 25), 1, customer);
        createInvoice(LocalDate.of(2024, Month.MARCH, 10), 2, customer);
        createInvoice(LocalDate.of(2024, Month.APRIL, 10), 1, customer);

        createCorrectionInvoice();

        entityManager.flush();
    }

    @Test
    void givenInvoice_whenMonthStrategyUsed_thenThreeReturned() {
        Invoice invoice = new Invoice();
        invoice.setIssueDate(LocalDate.of(2024, Month.MARCH, 26));

        assertEquals(3, monthCounterStrategy.getNextSerialNumber(invoice));
    }

    @Test
    void givenInvoice_whenContinuousStrategyUsed_thenFourReturned() {
        Invoice invoice = new Invoice();
        invoice.setIssueDate(LocalDate.of(2024, Month.MARCH, 26));

        assertEquals(4, continuousCounterStrategy.getNextSerialNumber(invoice));
    }

    private void createInvoice(LocalDate date, int serialNumber, Customer customer) {
        Invoice invoice = new Invoice();

        invoice.setBuyer(customer);
        invoice.setSeller(customer);
        invoice.setIssueDate(date);
        invoice.setSerialNumber(serialNumber);

        entityManager.persist(invoice);
    }

    private void createCorrectionInvoice() {
        CorrectionInvoice invoice = new CorrectionInvoice();

        invoice.setIssueDate(LocalDate.of(2024, Month.MARCH, 25));
        invoice.setSerialNumber(1);

        entityManager.persist(invoice);
    }
}