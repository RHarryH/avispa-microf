package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import lombok.RequiredArgsConstructor;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public abstract class BaseService<T extends EcmObject, D extends Dto, M extends IEntityDtoMapper<T, D>> implements IBaseService<T,D> {
    private final M entityDtoMapper;

    public M getEntityDtoMapper() {
        return entityDtoMapper;
    }
}
