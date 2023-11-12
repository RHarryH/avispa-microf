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
import com.avispa.ecm.model.base.dto.Dto;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
interface EcmService<T extends EcmObject, D extends Dto> {
    void add(D dto);

    void update(D dto, UUID id);

    void delete(UUID id);

    T findById(UUID id);

    List<D> findAll();
}
