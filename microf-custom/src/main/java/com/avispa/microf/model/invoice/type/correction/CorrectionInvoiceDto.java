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

package com.avispa.microf.model.invoice.type.correction;

import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.invoice.BaseInvoiceDto;
import com.avispa.microf.model.invoice.payment.PaidAmountDto;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.type.vat.InvoiceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jodconverter.core.util.StringUtils;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class CorrectionInvoiceDto extends BaseInvoiceDto {
    public static final String VM_CORRECTION_REASON_NO_LONGER = "The correction reason cannot be longer than 200 characters";

    @Size(max = 200, message = VM_CORRECTION_REASON_NO_LONGER)
    @DisplayName("Correction reason")
    private String correctionReason;

    private InvoiceDto originalInvoice;

    private @Valid PaidAmountDto payment = new PaidAmountDto();

    /**
     * Copy positions and comments from original invoice
     */
    @Override
    public void inherit() {
        this.setPositions(originalInvoice.getPositions().stream().map(PositionDto::new).toList());

        this.payment.setPaidAmount(originalInvoice.getPayment().getPaidAmount());
        this.payment.setPaidAmountDate(originalInvoice.getPayment().getPaidAmountDate());

        if (StringUtils.isNotEmpty(originalInvoice.getComments())) {
            this.setComments("Oryginalny komentarz: " + originalInvoice.getComments());
        }
    }
}
