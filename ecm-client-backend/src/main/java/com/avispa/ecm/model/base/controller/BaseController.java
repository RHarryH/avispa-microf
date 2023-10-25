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
import com.avispa.ecm.model.base.BaseService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.base.mapper.IEntityDtoMapper;
import com.avispa.ecm.util.error.ErrorUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseController<T extends EcmObject, D extends Dto, S extends BaseService<T, D, ? extends EcmEntityRepository<T>, ? extends IEntityDtoMapper<T, D>>> implements IBaseController<D>, IBaseModalableController {
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
        D dto = dtoService.createEmptyDtoInstance(request);

        BindingResult result = dtoService.bindObjectToDto(request, dto);

        add(dto, result);
    }

    @Override
    public void add(D dto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        T object = service.getEntityDtoMapper().convertToEntity(dto);
        service.add(object);
    }

    @PostMapping("/{id}")
    @Override
    public void update(HttpServletRequest request, @PathVariable("id") UUID id) {
        D dto = dtoService.createEmptyDtoInstance(request);
        dto.setId(id);

        BindingResult result = dtoService.bindObjectToDto(request, dto);

        update(dto, result);
    }

    @Override
    public void update(D dto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        service.update(dto);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
