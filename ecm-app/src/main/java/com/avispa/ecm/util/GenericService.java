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
