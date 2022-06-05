package com.avispa.microf.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.IDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class MultiTypeMapperRegistry {

    private final Map<String, IEntityDtoMapper<? extends EcmObject, ? extends IDto>> registry = new HashMap<>();

    public void registerMapper(String discriminator, IEntityDtoMapper<? extends EcmObject, ? extends IDto> mapper) {
        registry.put(discriminator, mapper);
    }

    @SuppressWarnings("unchecked")
    public <T extends EcmObject, D extends IDto> IEntityDtoMapper<T, D> getMapper(String discriminator) {
        if (!registry.containsKey(discriminator)) {
            throw new IllegalArgumentException("Missing registry entry for discriminator " + discriminator);
        }

        return (IEntityDtoMapper<T, D>) registry.get(discriminator);
    }
}