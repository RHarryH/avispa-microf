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
import com.avispa.ecm.util.exception.EcmException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseEcmController<T extends EcmObject, D extends Dto, S extends BaseEcmService<T, D, ? extends EcmEntityRepository<T>, ? extends EntityDtoMapper<T, D>>> implements EcmController<D>, EcmModalableController {
    @Getter
    private final S service;

    private DtoService dtoService;

    @Autowired
    public void setDtoService(DtoService dtoService) {
        this.dtoService = dtoService;
    }

    @PostMapping
    @Override
    public void add(HttpServletRequest request) {
        try {
            D dto = dtoService.parse(request.getReader(), extractTypeName(request));
            add(dto);
        } catch (IOException e) {
            throw new EcmException("Cannot get request data", e);
        }
    }

    @Override
    public void add(D dto) {
        T object = service.getEntityDtoMapper().convertToEntity(dto);
        service.add(object);
    }

    @PostMapping("/{id}")
    @Override
    public void update(HttpServletRequest request, @PathVariable("id") UUID id) {
        try {
            update(dtoService.parse(request.getReader(),
                    extractTypeName(request),
                    dto -> dto.setId(id)
            ));
        } catch (IOException e) {
            throw new EcmException("Cannot get request data", e);
        }
    }

    @Override
    public void update(D dto) {
        service.update(dto);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    /**
     * Extract type name from the URI
     * the pattern for the url is "v1/<type_name>/<others>"
     *
     * @param request
     * @return
     */
    private static String extractTypeName(HttpServletRequest request) {
        var requestUri = request.getRequestURI();
        var paths = requestUri.split("/");
        if(paths.length < 2) {
            throw new IllegalStateException("Cannot extract type from request path");
        } else {
            return TypeNameUtils.convertResourceNameToTypeName(paths[2]);
        }
    }
}
