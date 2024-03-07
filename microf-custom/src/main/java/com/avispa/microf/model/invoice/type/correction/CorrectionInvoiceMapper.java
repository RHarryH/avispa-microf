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

import com.avispa.microf.model.invoice.AbstractInvoiceMapper;
import com.avispa.microf.model.invoice.payment.Payment;
import com.avispa.microf.model.invoice.position.PositionMapper;
import com.avispa.microf.model.invoice.type.vat.Invoice;
import com.avispa.microf.model.invoice.type.vat.InvoiceDto;
import com.avispa.microf.model.invoice.type.vat.InvoiceMapper;
import com.avispa.microf.model.invoice.type.vat.InvoiceRepository;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {InvoiceMapper.class, PositionMapper.class})
public abstract class CorrectionInvoiceMapper extends AbstractInvoiceMapper<CorrectionInvoice, CorrectionInvoiceDto> {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @AfterMapping
    public void createNewPayment(CorrectionInvoiceDto source, @MappingTarget CorrectionInvoice target) {
        if (target.getOriginalInvoice() != null) {
            target.setPayment(new Payment(target.getOriginalInvoice().getPayment(),
                    source.getPayment().getPaidAmount(),
                    stringToLocalDate(source.getPayment().getPaidAmountDate())));
        }
    }

    @Override
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "originalInvoice", qualifiedByName = "originalInvoiceToInvoice")
    public abstract CorrectionInvoice convertToEntity(CorrectionInvoiceDto dto);

    @Override
    @Mapping(target = "originalInvoice", ignore = true)
    public abstract void updateEntityFromDto(CorrectionInvoiceDto dto, @MappingTarget CorrectionInvoice entity);

    /**
     * This mapping ensures the original invoice will remain untouched. It is always extracted
     * from the db based on the passed UUID.
     *
     * @param invoiceDto
     * @return
     */
    @Named("originalInvoiceToInvoice")
    protected Invoice originalInvoiceIdToInvoice(InvoiceDto invoiceDto) {
        return invoiceRepository.getReferenceById(invoiceDto.getId());
    }

    protected LocalDate stringToLocalDate(String date) {
        return Strings.isBlank(date) ? null : LocalDate.parse(date);
    }
}