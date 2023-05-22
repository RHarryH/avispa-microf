package com.avispa.ecm.model.base.mapper;

import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.EcmObject;
import org.mapstruct.MappingTarget;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rafał Hiszpański
 */
@Transactional
public interface IEntityDtoMapper<T extends EcmObject, D extends Dto> {
    D convertToDto(T entity);
    T convertToEntity(D dto);
    void updateEntityFromDto(D dto, @MappingTarget T entity);
}
