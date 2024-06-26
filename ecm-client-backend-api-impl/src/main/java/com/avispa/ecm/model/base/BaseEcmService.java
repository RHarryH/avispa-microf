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

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.mapper.EntityDtoMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public abstract class BaseEcmService<T extends EcmObject, D extends Dto, R extends EcmObjectRepository<T>, M extends EntityDtoMapper<T, D>> implements EcmService<T, D> {
    protected final R repository;
    @Getter
    protected final M entityDtoMapper;

    @Override
    @Transactional
    public void add(D dto) {
        T object = entityDtoMapper.convertToEntity(dto);
        repository.save(object);
        add(object);
    }

    protected void add(T object) {
        // NOTE: nothing extra
    }

    @Override
    @Transactional
    public void update(D dto, UUID id) {
        T object = findById(id);
        entityDtoMapper.updateEntityFromDto(dto, object);
        update(object);
    }

    protected void update(T object) {
        // NOTE: nothing extra
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<D> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, getSort());
        return repository
                .findAll(pageable).stream()
                .map(entityDtoMapper::convertToDto)
                .toList();
    }

    public List<D> findAll() {
        return repository
                .findAll(getSort()).stream()
                .map(entityDtoMapper::convertToDto)
                .toList();
    }

    private static Sort getSort() {
        return Sort.by(Sort.Direction.ASC, "creationDate");
    }

    public long count() {
        return repository.count();
    }
}
