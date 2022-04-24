package com.avispa.microf.model.invoice.position;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.avispa.microf.util.TestValidationUtils.validate;

/**
 * @author Rafał Hiszpański
 */
class PositionDtoValidationTest {
    private PositionDto positionDto;

    @BeforeEach
    void createDto() {
        positionDto = new PositionDto();
        positionDto.setObjectName("Name");
        positionDto.setUnit("HOUR");
        positionDto.setVatRate("VAT_08");
        positionDto.setUnitPrice(BigDecimal.ONE);
    }

    @Test
    void givenEmptyName_whenValidate_thenFail() {
        positionDto.setObjectName("");
        validate(positionDto, PositionDto.VM_POSITION_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenBlankName_whenValidate_thenFail() {
        positionDto.setObjectName("   \n\t");
        validate(positionDto, PositionDto.VM_POSITION_NOT_EMPTY_NOR_BLANK);
    }

    @Test
    void givenNameExceedMaxLength_whenValidate_thenFail() {
        positionDto.setObjectName(RandomStringUtils.randomAlphabetic(51));
        validate(positionDto, PositionDto.VM_POSITION_NAME_NO_LONGER);
    }

    @Test
    void givenNegativeQuantity_whenValidate_thenFail() {
        positionDto.setQuantity(new BigDecimal("-1.00"));
        validate(positionDto, PositionDto.VM_QUANTITY_POSITIVE);
    }

    @Test
    void givenQuantityExceedsMaxValue_whenValidate_thenFail() {
        positionDto.setQuantity(new BigDecimal("1234567.123"));
        validate(positionDto, PositionDto.VM_QUANTITY_OUT_OF_RANGE);
    }

    @Test
    void givenNullUnit_whenValidate_thenFail() {
        positionDto.setUnit(null);
        validate(positionDto, PositionDto.VM_UNIT_NOT_NULL);
    }

    @Test
    void givenNegativeUnitPrice_whenValidate_thenFail() {
        positionDto.setUnitPrice(new BigDecimal("-1.00"));
        validate(positionDto, PositionDto.VM_UNIT_PRICE_POSITIVE);
    }

    @Test
    void givenUnitPriceExceedsMaxValue_whenValidate_thenFail() {
        positionDto.setUnitPrice(new BigDecimal("123456789.12"));
        validate(positionDto, PositionDto.VM_UNIT_PRICE_OUT_OF_RANGE);
    }

    @Test
    void givenNullVatRate_whenValidate_thenFail() {
        positionDto.setVatRate(null);
        validate(positionDto, PositionDto.VM_VAT_RATE_NOT_NULL);
    }
}