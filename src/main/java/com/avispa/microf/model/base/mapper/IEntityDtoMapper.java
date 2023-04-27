package com.avispa.microf.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;
import org.mapstruct.MappingTarget;

/**
 * @author Rafał Hiszpański
 */
public interface IEntityDtoMapper<T extends EcmObject, D extends Dto> {
    D convertToDto(T entity);
    T convertToEntity(D dto);
    void updateEntityFromDto(D dto, @MappingTarget T entity);
}
