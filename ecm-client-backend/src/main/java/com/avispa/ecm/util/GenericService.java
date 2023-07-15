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

package com.avispa.ecm.util;

import com.avispa.ecm.model.base.BaseService;
import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.mapper.IEntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

/**
 * Service used to get entity service knowing only its type
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class GenericService {
    private final List<BaseService<? extends EcmObject, ? extends Dto, ? extends EcmObjectRepository<? extends EcmObject>, ?>> services;

    @SuppressWarnings("unchecked")
    public BaseService<EcmObject, Dto, EcmObjectRepository<EcmObject>, IEntityDtoMapper<EcmObject, Dto>> getService(Class<? extends EcmObject> entityClass) {
        return (BaseService<EcmObject, Dto, EcmObjectRepository<EcmObject>, IEntityDtoMapper<EcmObject, Dto>>) services.stream().filter(service -> {
                    Class<?> serviceEntityClass = getServiceEntity(service.getClass());
                    return entityClass.equals(serviceEntityClass);
                }).findFirst().orElseThrow();
    }

    private Class<?> getServiceEntity(Class<?> serviceClass) {
        Map<TypeVariable<?>, Type> typeArgs  = TypeUtils.getTypeArguments(serviceClass, BaseService.class);
        TypeVariable<?> argTypeParam =  BaseService.class.getTypeParameters()[0];
        java.lang.reflect.Type argType = typeArgs.get(argTypeParam);
        return TypeUtils.getRawType(argType, null);
    }
}
