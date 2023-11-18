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

package com.avispa.ecm.model.base;

import com.avispa.ecm.testdocument.TestDocumentMapper;
import com.avispa.ecm.testdocument.TestDocumentRepository;
import com.avispa.ecm.testdocument.TestDocumentService;
import com.avispa.ecm.testdocument.simple.TestDocument;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rafał Hiszpański
 */
@ExtendWith(MockitoExtension.class)
class BaseEcmServiceTest {
    @InjectMocks
    private TestDocumentService service;

    @Mock
    private TestDocumentMapper mapper;

    @Mock
    private TestDocumentRepository repository;

    @Test
    void givenDto_whenAdd_thenEntityAddedToRepo() {
        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Dto");

        TestDocument entity = new TestDocument();
        entity.setObjectName("Entity");

        when(mapper.convertToEntity(dto)).thenReturn(entity);

        service.add(dto);

        verify(repository).save(entity);
    }

    @Test
    void givenDtoAndId_whenUpdate_thenEntityUpdatedInRepo() {
        UUID id = UUID.randomUUID();
        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Dto");

        TestDocument entity = new TestDocument();
        entity.setObjectName("Entity");

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.update(dto, id);

        verify(mapper).updateEntityFromDto(dto, entity);
    }

    @Test
    void givenId_whenDelete_thenEntityRemovedFromRepo() {
        UUID id = UUID.randomUUID();

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void whenFindById_thenRepositoryCalled() {
        UUID id = UUID.randomUUID();
        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void whenFindAll_thenReturnDtos() {
        TestDocumentDto dto = new TestDocumentDto();
        dto.setObjectName("Dto");

        TestDocument entity = new TestDocument();
        entity.setObjectName("Entity");

        when(repository.findAll(any(Sort.class))).thenReturn(List.of(entity));
        when(mapper.convertToDto(entity)).thenReturn(dto);

        assertEquals(List.of(dto), service.findAll());
    }
}