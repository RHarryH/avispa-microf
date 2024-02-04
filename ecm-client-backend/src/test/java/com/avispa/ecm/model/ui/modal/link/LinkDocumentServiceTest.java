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

package com.avispa.ecm.model.ui.modal.link;

import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.model.ui.modal.link.dto.LinkDocumentDto;
import com.avispa.ecm.model.ui.modal.link.mapper.LinkDocumentDtoMapper;
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.util.exception.EcmException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class LinkDocumentServiceTest {
    private static final String TYPE_NAME = "Test document";

    private static LinkDocumentService linkDocumentService;
    private static ContextService contextService;
    private static TypeService typeService;

    @BeforeAll
    public static void init() {
        contextService = mock(ContextService.class);
        typeService = mock(TypeService.class);
        linkDocumentService = new LinkDocumentService(
                Mappers.getMapper(LinkDocumentDtoMapper.class), contextService, typeService);
    }


    @Test
    void givenTypeName_whenGetLinkDocument_thenReturn() {
        Type type = getType();
        when(typeService.getType(TYPE_NAME)).thenReturn(type);
        when(contextService.getConfiguration(TestDocument.class, LinkDocument.class)).thenReturn(Optional.of(getLinkDocument(type)));

        var result = linkDocumentService.get(TYPE_NAME);
        var expected = getLinkDocumentDto();

        assertEquals(expected, result);
    }

    @Test
    void givenTypeName_whenFindLinkDocument_thenReturn() {
        Type type = getType();
        when(typeService.getType(TYPE_NAME)).thenReturn(type);
        when(contextService.getConfiguration(TestDocument.class, LinkDocument.class)).thenReturn(Optional.of(getLinkDocument(type)));

        var result = linkDocumentService.find(TYPE_NAME);
        var expected = getLinkDocumentDto();

        assertEquals(expected, result);
    }

    @Test
    void givenTypeName_whenGetAndConfigNotFound_thenEcmException() {
        when(typeService.getType(TYPE_NAME)).thenReturn(getType());
        when(contextService.getConfiguration(TestDocument.class, LinkDocument.class)).thenReturn(Optional.empty());

        assertThrows(EcmException.class, () -> linkDocumentService.get(TYPE_NAME));
    }

    @Test
    void givenTypeName_whenFindAndConfigNotFound_thenNull() {
        when(typeService.getType(TYPE_NAME)).thenReturn(getType());
        when(contextService.getConfiguration(TestDocument.class, LinkDocument.class)).thenReturn(Optional.empty());

        assertNull(linkDocumentService.find(TYPE_NAME));
    }

    private Type getType() {
        Type type = new Type();
        type.setObjectName(TYPE_NAME);
        type.setEntityClass(TestDocument.class);

        return type;
    }

    private LinkDocument getLinkDocument(Type type) {
        LinkDocument linkDocument = new LinkDocument();
        linkDocument.setTitle("Title");
        linkDocument.setMessage("Message");
        linkDocument.setType(type);

        return linkDocument;
    }

    private LinkDocumentDto getLinkDocumentDto() {
        LinkDocumentDto linkDocumentDto = new LinkDocumentDto();
        linkDocumentDto.setTitle("Title");
        linkDocumentDto.setMessage("Message");
        linkDocumentDto.setType(TYPE_NAME);

        return linkDocumentDto;
    }

}