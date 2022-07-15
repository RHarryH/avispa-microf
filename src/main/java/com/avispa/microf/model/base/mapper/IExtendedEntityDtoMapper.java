package com.avispa.microf.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.base.dto.Dto;

/**
 * Includes conversion from entity to the Common DTO. When an entity which does not represent a multi type
 * convertToCommonDto should return the same result as convertToDto
 * @author Rafał Hiszpański
 */
public interface IExtendedEntityDtoMapper<T extends EcmObject, D extends Dto, C extends CommonDto> extends IEntityDtoMapper<T, D> {
    C convertToCommonDto(T entity);
}
