package com.avispa.microf.model.ui.widget.list.mapper;

import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import com.avispa.microf.model.invoice.InvoiceDto;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
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

    @Test
    void givenCommonDto_whenMaps_thenCorrect() {
        when(dictionaryService.getValueFromDictionary(InvoiceDto.class, "objectName", "test name")).thenReturn("test name");
        when(dictionaryService.getValueFromDictionary(InvoiceDto.class, "issueDate", "2022-07-14")).thenReturn("2022-07-14");

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setObjectName("test name");
        invoiceDto.setIssueDate(LocalDate.of(2022, Month.JULY, 14).format(DateTimeFormatter.ISO_LOCAL_DATE));
        invoiceDto.setHasPdfRendition(true);

        ListDataDto listDataDto = mapper.convert(invoiceDto, List.of("objectName", "issueDate"));

        assertEquals(invoiceDto.getId(), listDataDto.getId());
        assertEquals(invoiceDto.hasPdfRendition(), listDataDto.hasPdfRendition());
        assertEquals(List.of(invoiceDto.getObjectName(), invoiceDto.getIssueDate()), listDataDto.getValues());
    }
}