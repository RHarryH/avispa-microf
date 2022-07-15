package com.avispa.microf.util;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.mapper.IExtendedEntityDtoMapper;
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
    private final List<BaseService<? extends EcmObject, ? extends Dto, ? extends EcmObjectRepository<? extends EcmObject>, ? extends IExtendedEntityDtoMapper<? extends EcmObject, ? extends Dto, ? extends CommonDto>>> services;

    @SuppressWarnings("unchecked")
    public BaseService<EcmObject, Dto, EcmObjectRepository<EcmObject>, IExtendedEntityDtoMapper<EcmObject, Dto, CommonDto>> getService(Class<? extends EcmObject> entityClass) {
        return (BaseService<EcmObject, Dto, EcmObjectRepository<EcmObject>, IExtendedEntityDtoMapper<EcmObject, Dto, CommonDto>>) services.stream().filter(service -> {
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
