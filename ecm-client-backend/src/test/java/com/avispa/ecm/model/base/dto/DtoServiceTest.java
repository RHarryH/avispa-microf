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

package com.avispa.ecm.model.base.dto;

import com.avispa.ecm.EcmConfiguration;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.testdocument.discriminator.TestDocumentADto;
import com.avispa.ecm.testdocument.discriminator.TestDocumentCommonDto;
import com.avispa.ecm.testdocument.discriminator.TestDocumentWithDiscriminator;
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import com.avispa.ecm.util.GenericService;
import com.avispa.ecm.util.error.EcmDtoValidator;
import com.avispa.ecm.util.exception.EcmException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@JsonTest
@Import({DtoService.class, EcmDtoValidator.class, EcmConfiguration.class}) // required to load custom object mapper config
class DtoServiceTest {
    private static final String TEST_DOCUMENT_TYPE_NAME = "Test document";
    private static final String TEST_DOCUMENT_WITH_DISCRIMINATOR_TYPE_NAME = "Test document with discriminator";
    private static final String INPUT_JSON = "{\"type\":\"A\", \"objectName\": \"Test\", \"unitPrice\": \"1.00\"}";

    @Autowired
    private DtoService dtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TypeService typeService;

    @MockBean
    private DtoRepository dtoRepository;

    @MockBean
    private GenericService genericService;

    @TestConfiguration
    public static class DtoServiceTestConfiguration {
        @Bean
        public Validator localValidatorFactoryBean() {
            return new LocalValidatorFactoryBean();
        }
    }

    @Test
    void givenTypeWithoutDiscriminatorAndDtoConfig_whenConvertJsonTree_thenCorrectDtoIsReturned() {
        Type type = createType();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByEntityClassAndDiscriminatorIsNull(TestDocument.class)).thenReturn(Optional.of(createDtoObject(type)));

        TestDocumentDto testDocumentDto = dtoService.convert(getInputJsonTree(), TEST_DOCUMENT_TYPE_NAME);

        assertEquals("Test", testDocumentDto.getObjectName());
        assertEquals(new BigDecimal("1.00"), testDocumentDto.getUnitPrice());
    }

    @Test
    void givenTypeWithDiscriminatorAndDtoConfig_whenConvertJsonTree_thenCorrectDtoIsReturned() {
        Type type = createTypeWithDiscriminator();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByEntityClassAndDiscriminator(TestDocumentWithDiscriminator.class, "A")).thenReturn(Optional.of(createDtoObject(type, "A", TestDocumentADto.class)));

        TestDocumentADto testDocumentDto = dtoService.convert(getInputJsonTree(), TEST_DOCUMENT_TYPE_NAME);

        assertEquals("A", testDocumentDto.getType());
    }

    @Test
    void givenCommonDto_whenConvertCommonDto_thenCorrectDtoIsReturned() {
        Type type = createTypeWithDiscriminator();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByDtoClass(TestDocumentCommonDto.class)).thenReturn(Optional.of(createDtoObject(type, "", TestDocumentCommonDto.class)));
        when(dtoRepository.findByEntityClassAndDiscriminator(TestDocumentWithDiscriminator.class, "A")).thenReturn(Optional.of(createDtoObject(type, "A", TestDocumentADto.class)));

        TestDocumentCommonDto testDocumentCommonDto = createTestDocumentCommonDto();

        TestDocumentADto dto = dtoService.convertCommonToConcreteDto(testDocumentCommonDto);

        assertEquals("Test", dto.getObjectName());
    }

    @Test
    void givenCommonDtoWithMissingDtoConfiguration_whenConvertCommonDto_thenThrowException() {
        Type type = createTypeWithDiscriminator();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);

        TestDocumentCommonDto testDocumentCommonDto = createTestDocumentCommonDto();

        assertThrows(EcmException.class, () -> dtoService.convertCommonToConcreteDto(testDocumentCommonDto));
    }

    private Type createType() {
        Type type = new Type();
        type.setObjectName(TEST_DOCUMENT_TYPE_NAME);
        type.setEntityClass(TestDocument.class);

        return type;
    }

    private Type createTypeWithDiscriminator() {
        Type type = new Type();
        type.setObjectName(TEST_DOCUMENT_WITH_DISCRIMINATOR_TYPE_NAME);
        type.setEntityClass(TestDocumentWithDiscriminator.class);

        return type;
    }

    private DtoObject createDtoObject(Type type) {
        return createDtoObject(type, null, TestDocumentDto.class);
    }

    private DtoObject createDtoObject(Type type, String discriminatorValue, Class<? extends Dto> dtoClass) {
        DtoObject dtoObject = new DtoObject();
        dtoObject.setDtoClass(dtoClass);
        dtoObject.setDiscriminator(discriminatorValue);
        dtoObject.setType(type);

        return dtoObject;
    }

    @SneakyThrows
    private JsonNode getInputJsonTree() {
        return objectMapper.readTree(INPUT_JSON);
    }

    private static TestDocumentCommonDto createTestDocumentCommonDto() {
        TestDocumentCommonDto testDocumentCommonDto = new TestDocumentCommonDto();
        testDocumentCommonDto.setType("A");
        testDocumentCommonDto.setObjectName("Test");
        testDocumentCommonDto.setUnitPrice(BigDecimal.ONE);
        return testDocumentCommonDto;
    }
}