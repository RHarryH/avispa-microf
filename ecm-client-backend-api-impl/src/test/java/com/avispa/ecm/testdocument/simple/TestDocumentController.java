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

package com.avispa.ecm.testdocument.simple;

import com.avispa.ecm.model.base.controller.BaseSimpleEcmController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/test-document")
public class TestDocumentController extends BaseSimpleEcmController<TestDocument, TestDocumentDto, TestDocumentService> {
    protected TestDocumentController(TestDocumentService service) {
        super(service);
    }

    @Override
    protected void add(TestDocumentDto dto) {
        service.add(dto);
    }

    @Override
    protected void update(UUID id, TestDocumentDto dto) {
        service.update(dto, id);
    }
}
