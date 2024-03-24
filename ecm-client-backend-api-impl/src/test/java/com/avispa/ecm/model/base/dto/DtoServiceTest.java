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

package com.avispa.ecm.model.base.dto;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentADetailDto;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentADto;
import com.avispa.ecm.testdocument.multi.TestMultiDocumentCommonDto;
import com.avispa.ecm.testdocument.multi.TestMultiDocument;
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import com.avispa.ecm.util.exception.EcmException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest
class DtoServiceTest {
    private static final String TEST_DOCUMENT_TYPE_NAME = "Test document";
    private static final String TEST_MULTI_DOCUMENT_TYPE_NAME = "Test multi document";

    private static final String INPUT_JSON = "{\"objectName\": \"Test\", \"unitPrice\": \"1.00\"}";

    private static final String MULTI_INPUT_JSON = "{\"type\":\"A\", \"stringValue\":\"Hello\"}";

    @Autowired
    private DtoService dtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TypeService typeService;

    @MockBean
    private DtoRepository dtoRepository;

    @TestConfiguration
    public static class DtoServiceTestConfiguration {
        @Bean
        public Validator localValidatorFactoryBean() {
            return new LocalValidatorFactoryBean();
        }
    }

    @Test
    void givenTypeName_whenSearchForDtoObject_thenCorrectMethodCalled() {
        when(dtoRepository.findByTypeNameAndDiscriminatorIsNull("Document")).thenReturn(Optional.of(new DtoObject()));

        dtoService.getDtoObjectFromTypeName("Document");

        verify(dtoRepository).findByTypeNameAndDiscriminatorIsNull("Document");
    }

    @Test
    void givenType_whenSearchForDtoObject_thenCorrectMethodCalled() {
        Type type = new Type();
        type.setObjectName("Document");

        when(dtoRepository.findByTypeAndDiscriminatorIsNull(type)).thenReturn(Optional.of(new DtoObject()));

        dtoService.getDtoObjectFromType(type);

        verify(dtoRepository).findByTypeAndDiscriminatorIsNull(type);
    }

    @Test
    void givenObject_whenConvertToDto_thenServiceExtracted() {
        EcmObject object = new TestDocument();

        var dto = dtoService.convertObjectToDto(object);
        assertInstanceOf(TestDocumentDto.class, dto);
    }

    @Test
    void givenTypeWithoutDiscriminatorAndDtoConfig_whenConvertJsonTree_thenCorrectDtoIsReturned() {
        Type type = createType();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByEntityClassAndDiscriminatorIsNull(TestDocument.class)).thenReturn(Optional.of(createDtoObject(type)));

        TestDocumentDto testDocumentDto = dtoService.convert(getInputJsonTree(INPUT_JSON), TEST_DOCUMENT_TYPE_NAME);

        Assertions.assertEquals("Test", testDocumentDto.getObjectName());
        Assertions.assertEquals(new BigDecimal("1.00"), testDocumentDto.getUnitPrice());
    }

    @Test
    void givenTypeWithDiscriminatorAndDtoConfig_whenConvertJsonTree_thenCorrectDtoIsReturned() {
        Type type = createMultiType();
        when(typeService.getType(TEST_MULTI_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByEntityClassAndDiscriminator(TestMultiDocument.class, "A")).thenReturn(Optional.of(createDtoObject(type, "A", TestMultiDocumentADto.class)));

        TestMultiDocumentADto testDocumentDto = dtoService.convert(getInputJsonTree(MULTI_INPUT_JSON), TEST_MULTI_DOCUMENT_TYPE_NAME);

        assertEquals("Hello", testDocumentDto.getDetails().getStringValue());
    }

    @Test
    void givenCommonDto_whenConvertCommonDto_thenCorrectDtoIsReturned() {
        Type type = createMultiType();
        when(typeService.getType(TEST_MULTI_DOCUMENT_TYPE_NAME)).thenReturn(type);
        when(dtoRepository.findByDtoClass(TestMultiDocumentCommonDto.class)).thenReturn(Optional.of(createDtoObject(type, "", TestMultiDocumentCommonDto.class)));
        when(dtoRepository.findByEntityClassAndDiscriminator(TestMultiDocument.class, "A")).thenReturn(Optional.of(createDtoObject(type, "A", TestMultiDocumentADto.class)));

        TestMultiDocumentCommonDto testMultiDocumentCommonDto = createTestDocumentCommonDto();

        TestMultiDocumentADto dto = dtoService.convertCommonToConcreteDto(testMultiDocumentCommonDto);

        Assertions.assertEquals("Test", dto.getDetails().getStringValue());
    }

    @Test
    void givenCommonDtoWithMissingDtoConfiguration_whenConvertCommonDto_thenThrowException() {
        Type type = createMultiType();
        when(typeService.getType(TEST_DOCUMENT_TYPE_NAME)).thenReturn(type);

        TestMultiDocumentCommonDto testMultiDocumentCommonDto = createTestDocumentCommonDto();

        assertThrows(EcmException.class, () -> dtoService.convertCommonToConcreteDto(testMultiDocumentCommonDto));
    }

    private Type createType() {
        Type type = new Type();
        type.setObjectName(TEST_DOCUMENT_TYPE_NAME);
        type.setEntityClass(TestDocument.class);

        return type;
    }

    private Type createMultiType() {
        Type type = new Type();
        type.setObjectName(TEST_MULTI_DOCUMENT_TYPE_NAME);
        type.setEntityClass(TestMultiDocument.class);

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
    private JsonNode getInputJsonTree(String json) {
        return objectMapper.readTree(json);
    }

    private static TestMultiDocumentCommonDto createTestDocumentCommonDto() {
        TestMultiDocumentADetailDto testMultiDocumentADetailDto = new TestMultiDocumentADetailDto();
        testMultiDocumentADetailDto.setStringValue("Test");

        TestMultiDocumentCommonDto testMultiDocumentCommonDto = new TestMultiDocumentCommonDto();
        testMultiDocumentCommonDto.setType("A");
        testMultiDocumentCommonDto.setTestMultiDocumentADetailDto(testMultiDocumentADetailDto);
        return testMultiDocumentCommonDto;
    }
}