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

package com.avispa.ecm.model.ui.modal.link.mapper;

import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.ui.modal.link.LinkDocument;
import com.avispa.ecm.model.ui.modal.link.dto.LinkDocumentDto;
import com.avispa.ecm.testdocument.simple.TestDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class LinkDocumentDtoMapperTest {
    private final LinkDocumentDtoMapper mapper = Mappers.getMapper(LinkDocumentDtoMapper.class);

    @Test
    void givenLinkDocument_whenMaps_thenCorrectDto() {
        LinkDocument linkDocument = new LinkDocument();
        linkDocument.setTitle("Title");
        linkDocument.setMessage("Message");

        Type type = new Type();
        type.setObjectName("Test document");
        type.setEntityClass(TestDocument.class);

        linkDocument.setType(type);

        LinkDocumentDto linkDocumentDto = mapper.convert(linkDocument);

        assertEquals("Test document", linkDocumentDto.getType());
        assertEquals("Title", linkDocumentDto.getTitle());
        assertEquals("Message", linkDocumentDto.getMessage());
    }
}