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

package com.avispa.ecm.model.base.controller;

import com.avispa.ecm.model.EcmEntityRepository;
import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.base.BaseEcmService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.base.mapper.EntityDtoMapper;
import com.avispa.ecm.util.TypeNameUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@RequestMapping("/v1/{resourceName}")
@Slf4j
public abstract class BaseEcmController<T extends EcmObject, D extends Dto, S extends BaseEcmService<T, D, ? extends EcmEntityRepository<T>, ? extends EntityDtoMapper<T, D>>> implements EcmController<D>, EcmModalableController {
    @Getter
    private final S service;

    private DtoService dtoService;

    @Autowired
    public void setDtoService(DtoService dtoService) {
        this.dtoService = dtoService;
    }

    @Override
    public void add(JsonNode payload, @PathVariable String resourceName) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);
        D dto = dtoService.convert(payload, typeName);
        add(dto);
    }

    @Override
    public void add(D dto) {
        T object = service.getEntityDtoMapper().convertToEntity(dto);
        service.add(object);
    }

    @Override
    public void update(JsonNode payload, @PathVariable String resourceName, UUID id) {
        String typeName = TypeNameUtils.convertResourceNameToTypeName(resourceName);
        update(dtoService.convert(payload,
                typeName,
                dto -> dto.setId(id)
        ));
    }

    @Override
    public void update(D dto) {
        service.update(dto);
    }

    @Override
    public void delete(UUID id, @PathVariable String resourceName) {
        service.delete(id);
    }
}
