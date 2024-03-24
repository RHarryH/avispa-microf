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

package com.avispa.ecm.util;

import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.testdocument.simple.TestDocumentService;
import com.avispa.ecm.testdocument.simple.TestDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest
class GenericServiceTest {
    @Autowired
    private GenericService genericService;

    @Test
    void givenClassName_whenGetService_thenServiceIsReturned() {
        assertInstanceOf(TestDocumentService.class, genericService.getService(TestDocument.class));
    }

    @Test
    void givenClassName_whenGetService_thenServiceNotFound() {
        assertThrows(NoSuchElementException.class, () -> genericService.getService(Document.class));
    }
}