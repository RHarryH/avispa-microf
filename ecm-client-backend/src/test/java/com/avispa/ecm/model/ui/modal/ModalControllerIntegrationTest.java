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

package com.avispa.ecm.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.base.dto.DtoObject;
import com.avispa.ecm.model.configuration.EcmConfig;
import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.configuration.context.Context;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.model.content.Content;
import com.avispa.ecm.model.format.Format;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.testdocument.TestDocument;
import com.avispa.ecm.testdocument.TestDocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
@ActiveProfiles("test")
class ModalControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EcmObjectRepository<EcmObject> ecmObjectRepository;

    @Autowired
    private EcmConfigRepository<EcmConfig> ecmConfigRepository;

    @Test
    void givenEmptyConfiguration_whenGetAddModal_thenReturn500() {
        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/add/test-document", ModalDto.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void givenMissingPropertyPage_whenGetAddModal_thenReturn500() {
        Type type = createType();
        createDtoObject(type);

        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/add/test-document", ModalDto.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void givenFullData_whenGetAddModal_thenReturn200() {
        Type type = createType();
        createDtoObject(type);
        PropertyPage propertyPage = createPropertyPage("testPropertyPage.json");
        Upsert upsert = createUpsert(propertyPage);
        createContext(type, upsert);

        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/add/test-document", ModalDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void givenEmptyConfiguration_whenGetUpdateModal_thenReturn500() {
        TestDocument testDocument = ecmObjectRepository.save( new TestDocument());

        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/update/test-document/" + testDocument.getId(), ModalDto.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void givenMissingPropertyPage_whenGetUpdateModal_thenReturn500() {
        Type type = createType();
        createDtoObject(type);

        TestDocument testDocument = ecmObjectRepository.save( new TestDocument());

        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/update/test-document/" + testDocument.getId(), ModalDto.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void givenFullDataButMissingObject_whenGetUpdateModal_thenReturn404() {
        Type type = createType();
        PropertyPage propertyPage = createPropertyPage("testPropertyPage.json");
        Upsert upsert = createUpsert(propertyPage);
        createContext(type, upsert);

        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/update/test-document/" + UUID.randomUUID(), ModalDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void givenFullData_whenGetUpdateModal_thenReturn200() {
        Type type = createType();
        PropertyPage propertyPage = createPropertyPage("testPropertyPage.json");
        Upsert upsert = createUpsert(propertyPage);
        createContext(type, upsert);

        TestDocument testDocument = ecmObjectRepository.save( new TestDocument());

        ResponseEntity<ModalDto> response = this.restTemplate.getForEntity("http://localhost:" + port + "/v1/modal/update/test-document/" + testDocument.getId(), ModalDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Type createType() {
        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        ecmObjectRepository.save(type);
        return type;
    }

    private DtoObject createDtoObject(Type type) {
        DtoObject dtoObject = new DtoObject();
        dtoObject.setDtoClass(TestDocumentDto.class);
        dtoObject.setType(type);

        ecmObjectRepository.save(dtoObject);
        return dtoObject;
    }

    private PropertyPage createPropertyPage(String contentPath) {
        PropertyPage propertyPage = new PropertyPage();

        propertyPage = ecmConfigRepository.save(propertyPage);

        Content content = createContent(contentPath);
        content.setRelatedEntity(propertyPage);
        ecmObjectRepository.save(content);

        propertyPage.setContents(Set.of(content));

        return propertyPage;
    }

    private Content createContent(String contentPath) {
        Content content = new Content();
        content.setFileStorePath(getPath(contentPath));
        Format format = new Format();
        format.setObjectName("pdf");
        content.setFormat(format);

        ecmObjectRepository.save(format);

        return content;
    }

    private String getPath(String resource) {
        try {
            return new ClassPathResource(resource).getFile().getAbsolutePath();
        } catch (IOException e) {
            log.error("Resource {} does not exist", resource);
        }

        return "";
    }

    private Upsert createUpsert(PropertyPage propertyPage) {
        Upsert upsert = new Upsert();
        upsert.setPropertyPage(propertyPage);
        ecmConfigRepository.save(upsert);
        return upsert;
    }

    private void createContext(Type type, EcmConfig... ecmConfigs) {
        Context context = new Context();
        context.setObjectName(RandomStringUtils.randomAlphanumeric(10));
        context.setEcmConfigs(List.of(ecmConfigs));
        context.setType(type);
        context.setMatchRule("{}");
        context.setImportance(1);
        ecmConfigRepository.save(context);
    }
}