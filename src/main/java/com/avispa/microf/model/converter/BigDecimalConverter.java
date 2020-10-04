package com.avispa.microf.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

/**
 * @author Rafał Hiszpański
 */
@Converter
public class BigDecimalConverter implements AttributeConverter<BigDecimal, Long> {

    @Override
    public Long convertToDatabaseColumn(BigDecimal value) {
        if (value == null) {
            return null;
        } else {
            return value.multiply(BigDecimal.valueOf(100)).longValue();
        }
    }

    @Override
    public BigDecimal convertToEntityAttribute(Long value) {
        if (value == null) {
            return null;
        } else {
            return new BigDecimal(value).divide(BigDecimal.valueOf(100));
        }
    }
}
