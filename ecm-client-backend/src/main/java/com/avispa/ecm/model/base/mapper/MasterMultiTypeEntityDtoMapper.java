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
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Rafał Hiszpański
 */
public abstract class MasterMultiTypeEntityDtoMapper<T extends EcmObject, D extends Dto> implements EntityDtoMapper<T, D> {
    @Autowired
    private MultiTypeMapperRegistry multiTypeMapperRegistry;

    @PostConstruct
    public void registerMappers() {
        registerMappers(multiTypeMapperRegistry);
    }

    protected abstract void registerMappers(MultiTypeMapperRegistry multiTypeMapperRegistry);

    @Override
    @SuppressWarnings("unchecked")
    public D convertToDto(T entity){
        return (D) getActualMapper(getDiscriminator(entity)).convertToDto(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convertToEntity(D dto) {
        return (T) getActualMapper(getDiscriminator(dto)).convertToEntity(dto);
    }

    @Override
    public void updateEntityFromDto(D dto, @MappingTarget T entity) {
        getActualMapper(getDiscriminator(dto)).updateEntityFromDto(dto, entity);
    }

    private <A extends EcmObject, B extends Dto> EntityDtoMapper<A, B> getActualMapper(String discriminator) {
        return multiTypeMapperRegistry.getMapper(discriminator);
    }

    protected abstract String getDiscriminator(T entity);
    protected abstract String getDiscriminator(D dto);
}
