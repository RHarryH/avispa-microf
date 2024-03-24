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

package com.avispa.ecm.testdocument;

import com.avispa.ecm.model.base.BaseEcmService;
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import com.avispa.ecm.util.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class TestDocumentService extends BaseEcmService<TestDocument, TestDocumentDto, TestDocumentRepository, TestDocumentMapper> {

    @Autowired
    public TestDocumentService(TestDocumentRepository testDocumentRepository, TestDocumentMapper entityDtoMapper) {
        super(testDocumentRepository, entityDtoMapper);
    }

    @Override
    public TestDocument findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TestDocument.class));
    }
}