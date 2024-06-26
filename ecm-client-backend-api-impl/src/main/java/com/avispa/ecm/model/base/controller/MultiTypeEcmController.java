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
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public abstract class MultiTypeEcmController<T extends EcmObject, C extends Dto, D extends Dto, S extends BaseEcmService<T, D, ? extends EcmEntityRepository<T>, ? extends EntityDtoMapper<T, D>>> implements EcmController<C> {
    protected final S service;

    @Override
    public void delete(UUID id) {
        service.delete(id);
    }
}
