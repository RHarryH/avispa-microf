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
