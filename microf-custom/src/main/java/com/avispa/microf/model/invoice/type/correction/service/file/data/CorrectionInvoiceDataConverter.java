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

package com.avispa.microf.model.invoice.type.correction.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import com.avispa.microf.model.invoice.payment.PaymentDto;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class CorrectionInvoiceDataConverter {
    private final DictionaryService dictionaryService;

    public CorrectionInvoiceData convert(CorrectionInvoice invoice) {
        Dictionary unitDict = dictionaryService.getDictionary(PositionDto.class, "unit");
        Dictionary vatRateDict = dictionaryService.getDictionary(PositionDto.class, "vatRate");
        Dictionary paymentMethodDict = dictionaryService.getDictionary(PaymentDto.class, "method");

        return new CorrectionInvoiceData(invoice, unitDict, vatRateDict, paymentMethodDict);
    }
}
