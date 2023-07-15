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

package com.avispa.ecm.model.ui.widget.list.mapper;

import com.avispa.ecm.model.ui.widget.list.dto.ListDataDto;
import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class ListDataDtoMapperIntegrationTest {

    @Mock
    private DictionaryService dictionaryService;

    @InjectMocks
    private final ListDataDtoMapper mapper = Mappers.getMapper(ListDataDtoMapper.class);

    /*@Test
    void givenCommonDto_whenMaps_thenCorrect() {
        when(dictionaryService.getValueFromDictionary(InvoiceDto.class, "objectName", "test name")).thenReturn("test name");
        when(dictionaryService.getValueFromDictionary(InvoiceDto.class, "issueDate", "2022-07-14")).thenReturn("2022-07-14");
        when(dictionaryService.getValueFromDictionary(InvoiceDto.class, "pdfRenditionAvailable", "true")).thenReturn("true");

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setObjectName("test name");
        invoiceDto.setIssueDate(LocalDate.of(2022, Month.JULY, 14).format(DateTimeFormatter.ISO_LOCAL_DATE));
        invoiceDto.setPdfRenditionAvailable(true);

        ListDataDto listDataDto = mapper.convert(invoiceDto, List.of("objectName", "issueDate", "pdfRenditionAvailable"));

        assertEquals(invoiceDto.getId(), listDataDto.getId());
        assertEquals(Map.of("objectName", invoiceDto.getObjectName(), "issueDate", invoiceDto.getIssueDate(), "pdfRenditionAvailable", Boolean.toString(invoiceDto.isPdfRenditionAvailable())), listDataDto.getValues());
    }*/
}