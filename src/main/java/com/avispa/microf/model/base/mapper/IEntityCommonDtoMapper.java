package com.avispa.microf.model.base.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.CommonDto;

/**
 * @author Rafał Hiszpański
 */
public interface IEntityCommonDtoMapper<T extends EcmObject, C extends CommonDto> {
    C convertToCommonDto(T entity);
}
