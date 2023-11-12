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
import com.avispa.ecm.model.base.mapper.EntityDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.UUID;

import static com.avispa.ecm.util.error.ValidationErrorUtil.processErrors;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public abstract class BaseSimpleEcmController<T extends EcmObject, D extends Dto, S extends BaseEcmService<T, D, ? extends EcmEntityRepository<T>, ? extends EntityDtoMapper<T, D>>> extends SimpleEcmController<T, D, S> {
    protected BaseSimpleEcmController(S service) {
        super(service);
    }

    @Override
    public void add(@Valid D dto, BindingResult bindingResult) {
        processErrors(bindingResult);
        add(dto);
    }

    @Override
    public void update(UUID id, @Valid D dto, BindingResult bindingResult) {
        processErrors(bindingResult);
        update(id, dto);
    }

    protected abstract void add(D dto);

    protected abstract void update(UUID id, D dto);
}
