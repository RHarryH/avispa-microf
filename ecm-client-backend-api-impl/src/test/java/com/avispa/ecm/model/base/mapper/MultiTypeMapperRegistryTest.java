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

package com.avispa.ecm.model.base.mapper;

import com.avispa.ecm.testdocument.simple.TestDocumentMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Rafał Hiszpański
 */
class MultiTypeMapperRegistryTest {
    private static final MultiTypeMapperRegistry mapperRegistry = new MultiTypeMapperRegistry();

    @BeforeAll
    static void init() {
        mapperRegistry.registerMapper("A", Mappers.getMapper(TestDocumentMapper.class));
    }

    @Test
    void givenDiscriminator_whenGetMapper_thenMapperIsReturned() {
        assertNotNull(mapperRegistry.getMapper("A"));
    }

    @Test
    void givenWrongDiscriminator_whenGetMapper_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> mapperRegistry.getMapper("B"));
    }
}