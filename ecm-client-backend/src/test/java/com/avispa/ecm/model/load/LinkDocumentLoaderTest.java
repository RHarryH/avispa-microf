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

package com.avispa.ecm.model.load;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.load.dto.LinkDocumentDto;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.model.ui.modal.link.LinkDocument;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@SpringBootTest
class LinkDocumentLoaderTest {

    @MockBean
    private EcmConfigRepository<LinkDocument> repository;

    @MockBean
    private TypeService typeService;

    @Autowired
    private LinkDocumentLoader loader;

    @Captor
    private ArgumentCaptor<LinkDocument> entityCaptor;

    @Test
    void givenLinkDocumentDto_whenLoad_thenProperMethodsInvoked() {
        final String configName = "Default link document config";
        LinkDocumentDto dto = new LinkDocumentDto();
        dto.setName(configName);
        dto.setType("Document");
        dto.setMessage("Message");
        dto.setTitle("Title");

        Type type = new Type();
        type.setObjectName("Document");
        type.setEntityClass(Document.class);

        when(typeService.getType("Document")).thenReturn(type);
        when(repository.findByObjectName(configName)).thenReturn(Optional.empty());

        loader.load(dto, true);

        verify(repository).save(entityCaptor.capture());

        LinkDocument linkDocument = entityCaptor.getValue();
        assertEquals(configName, linkDocument.getObjectName());
        assertEquals(type, linkDocument.getType());
        assertEquals("Message", linkDocument.getMessage());
        assertEquals("Title", linkDocument.getTitle());
    }
}