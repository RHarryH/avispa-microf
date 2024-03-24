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

package com.avispa.ecm.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.base.dto.Dto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class MultiTypeMapperRegistry {

    private final Map<String, EntityDtoMapper<? extends EcmObject, ? extends Dto>> registry = new HashMap<>();

    public void registerMapper(String discriminator, EntityDtoMapper<? extends EcmObject, ? extends Dto> mapper) {
        registry.put(discriminator, mapper);
    }

    @SuppressWarnings("unchecked")
    public <T extends EcmObject, D extends Dto> EntityDtoMapper<T, D> getMapper(String discriminator) {
        if (!registry.containsKey(discriminator)) {
            throw new IllegalArgumentException("Missing registry entry for discriminator " + discriminator);
        }

        return (EntityDtoMapper<T, D>) registry.get(discriminator);
    }
}