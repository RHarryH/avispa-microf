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

package com.avispa.microf.model.invoice.position;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class PositionMapperTest {
    private final PositionMapper mapper = Mappers.getMapper(PositionMapper.class);

    @Test
    void givenEntity_whenConvert_thenCorrectDto() {
        Position position = getSampleEntity();
        PositionDto convertedDto = mapper.convertToDto(position);

        assertAll(
                () -> assertEquals("Position", convertedDto.getObjectName()),
                () -> assertEquals(BigDecimal.ONE, convertedDto.getQuantity()),
                () -> assertEquals("HOUR", convertedDto.getUnit()),
                () -> assertEquals(BigDecimal.ONE, convertedDto.getUnitPrice()),
                () -> assertEquals(BigDecimal.ZERO, convertedDto.getDiscount()),
                () -> assertEquals("VAT_23", convertedDto.getVatRate())
        );
    }

    @Test
    void givenDto_whenConvert_thenCorrectEntity() {
        PositionDto positionDto = getSampleDto();
        Position convertedEntity = mapper.convertToEntity(positionDto);

        assertAll(
                () -> assertEquals("Position DTO", convertedEntity.getObjectName()),
                () -> assertEquals(BigDecimal.TEN, convertedEntity.getQuantity()),
                () -> assertEquals("PIECE", convertedEntity.getUnit()),
                () -> assertEquals(BigDecimal.TEN, convertedEntity.getUnitPrice()),
                () -> assertEquals(BigDecimal.ONE, convertedEntity.getDiscount()),
                () -> assertEquals("VAT_00", convertedEntity.getVatRate())
        );
    }

    @Test
    void givenDtoAndEntity_whenUpdate_thenEntityHasDtoProperties() {
        Position position = getSampleEntity();
        PositionDto positionDto = getSampleDto();
        mapper.updateEntityFromDto(positionDto, position);

        assertAll(
                () -> assertEquals("Position DTO", position.getObjectName()),
                () -> assertEquals(BigDecimal.TEN, position.getQuantity()),
                () -> assertEquals("PIECE", position.getUnit()),
                () -> assertEquals(BigDecimal.TEN, position.getUnitPrice()),
                () -> assertEquals(BigDecimal.ONE, position.getDiscount()),
                () -> assertEquals("VAT_00", position.getVatRate())
        );
    }

    @Test
    void givenDtoAndEmptyEntity_whenUpdate_thenEntityHasDtoProperties() {
        Position position = new Position();
        PositionDto positionDto = getSampleDto();
        mapper.updateEntityFromDto(positionDto, position);

        assertAll(
                () -> assertEquals("Position DTO", position.getObjectName()),
                () -> assertEquals(BigDecimal.TEN, position.getQuantity()),
                () -> assertEquals("PIECE", position.getUnit()),
                () -> assertEquals(BigDecimal.TEN, position.getUnitPrice()),
                () -> assertEquals(BigDecimal.ONE, position.getDiscount()),
                () -> assertEquals("VAT_00", position.getVatRate())
        );
    }

    @Test
    void givenNullDto_whenUpdate_thenDoNothing() {
        Position position = getSampleEntity();
        mapper.updateEntityFromDto(null, position);

        assertAll(
                () -> assertEquals("Position", position.getObjectName()),
                () -> assertEquals(BigDecimal.ONE, position.getQuantity()),
                () -> assertEquals("HOUR", position.getUnit()),
                () -> assertEquals(BigDecimal.ONE, position.getUnitPrice()),
                () -> assertEquals(BigDecimal.ZERO, position.getDiscount()),
                () -> assertEquals("VAT_23", position.getVatRate())
        );
    }

    private Position getSampleEntity() {
        Position position = new Position();
        position.setObjectName("Position");
        position.setQuantity(BigDecimal.ONE);
        position.setUnit("HOUR");
        position.setUnitPrice(BigDecimal.ONE);
        position.setDiscount(BigDecimal.ZERO);
        position.setVatRate("VAT_23");

        return position;
    }

    private PositionDto getSampleDto() {
        PositionDto positionDto = new PositionDto();
        positionDto.setObjectName("Position DTO");
        positionDto.setQuantity(BigDecimal.TEN);
        positionDto.setUnit("PIECE");
        positionDto.setUnitPrice(BigDecimal.TEN);
        positionDto.setDiscount(BigDecimal.ONE);
        positionDto.setVatRate("VAT_00");

        return positionDto;
    }
}