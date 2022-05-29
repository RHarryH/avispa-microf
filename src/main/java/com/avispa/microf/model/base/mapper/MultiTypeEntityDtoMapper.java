package com.avispa.microf.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Rafał Hiszpański
 */
public abstract class MultiTypeEntityDtoMapper<T extends EcmObject, D extends Dto> implements IEntityDtoMapper<T, D> {
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

    private <A extends EcmObject, B extends Dto> IEntityDtoMapper<A, B> getActualMapper(String discriminator) {
        return multiTypeMapperRegistry.getMapper(discriminator);
    }

    protected abstract String getDiscriminator(T entity);
    protected abstract String getDiscriminator(D dto);
}
