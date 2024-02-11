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

package com.avispa.microf.model.invoice.type.vat;

import com.avispa.ecm.model.configuration.display.annotation.DisplayName;
import com.avispa.microf.model.invoice.BaseInvoiceDto;
import com.avispa.microf.model.invoice.payment.PaymentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class InvoiceDto extends BaseInvoiceDto {
    public static final String VM_SELLER_NOT_NULL = "Seller cannot be null";
    public static final String VM_BUYER_NOT_NULL = "Buyer cannot be null";
    public static final String VM_PAYMENT_NOT_EMPTY = "Payment cannot be empty";

    private UUID id;

    @NotNull(message = VM_SELLER_NOT_NULL)
    @DisplayName("Seller")
    private String seller;

    @NotNull(message = VM_BUYER_NOT_NULL)
    @DisplayName("Buyer")
    private String buyer;

    @DisplayName("Service Date")
    private String serviceDate;

    @NotNull(message = VM_PAYMENT_NOT_EMPTY)
    private @Valid PaymentDto payment;

    public InvoiceDto() {
        super();
        this.payment = new PaymentDto();
    }
}
