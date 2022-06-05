package com.avispa.microf.model.invoice.service.file.data;

import com.avispa.ecm.model.configuration.dictionary.Dictionary;
import com.avispa.ecm.model.configuration.dictionary.DictionaryService;
import com.avispa.microf.model.invoice.Invoice;
import com.avispa.microf.model.invoice.position.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class InvoiceDataConverter {
    private final DictionaryService dictionaryService;

    public InvoiceData convert(Invoice invoice) {
        Dictionary unitDict = dictionaryService.getDictionary(dictionaryService.getDictionaryNameFromAnnotation(PositionDto.class, "unit"));
        Dictionary vatRateDict = dictionaryService.getDictionary(dictionaryService.getDictionaryNameFromAnnotation(PositionDto.class, "vatRate"));

        return new InvoiceData(invoice, unitDict, vatRateDict);
    }
}
