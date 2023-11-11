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
import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.testdocument.TestDocument;
import com.avispa.ecm.testdocument.TestDocumentADto;
import com.avispa.ecm.testdocument.TestDocumentDto;
import com.avispa.ecm.testdocument.TestDocumentWithDiscriminator;
import com.avispa.ecm.util.GenericService;
import com.avispa.ecm.util.error.EcmDtoValidator;
import com.avispa.ecm.util.exception.RepositoryCorruptionError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void givenTypeWithoutDiscriminatorAndDtoConfig_whenConvertJsonWithEnrichment_thenObjectIsEnriched() {
        Type type = createType();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByEntityClassAndDiscriminatorIsNull(TestDocument.class)).thenReturn(Optional.of(createDtoObject(type)));

        TestDocumentDto testDocumentDto = dtoService.convert(getInputJsonTree(), TEST_DOCUMENT_TYPE_NAME, dto -> dto.setId(UUID.randomUUID()));

        assertNotNull(testDocumentDto.getId());
    }

    @Test
    void givenTypeWithoutDiscriminatorWithoutDtoConfig_whenConvertJson_thenExceptionIsThrown() {
        Type type = createType();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);

        var node = getInputJsonTree();
        assertThrows(RepositoryCorruptionError.class, () -> dtoService.convert(node, TEST_DOCUMENT_TYPE_NAME));
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
    void givenTypeWithUnknownDiscriminatorAndDtoConfig_whenConvertJson_thenExceptionIsThrown() {
        Type type = createTypeWithDiscriminator();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByEntityClassAndDiscriminator(TestDocumentWithDiscriminator.class, "C")).thenReturn(Optional.of(createDtoObject(type, "A", TestDocumentADto.class)));

        var node = getInputJsonTree();
        assertThrows(RepositoryCorruptionError.class, () -> dtoService.convert(node, TEST_DOCUMENT_TYPE_NAME));
    }

    private static Type createType() {
        return createType(TEST_DOCUMENT_TYPE_NAME, TestDocument.class);
    }

    private static Type createTypeWithDiscriminator() {
        return createType(TEST_DOCUMENT_WITH_DISCRIMINATOR_TYPE_NAME, TestDocumentWithDiscriminator.class);
    }

    private static Type createType(String typeName, Class<? extends EcmObject> typeClass) {
        Type type = new Type();
        type.setObjectName(typeName);
        type.setEntityClass(typeClass);

        return type;
    }

    private static DtoObject createDtoObject(Type type) {
        return createDtoObject(type, null, TestDocumentDto.class);
    }

    private static DtoObject createDtoObject(Type type, String discriminatorValue, Class<? extends Dto> dtoClass) {
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
}