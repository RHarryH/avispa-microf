package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.IDto;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IBaseService<T extends EcmObject, D extends IDto> {
    void add(T object);
    void update(D dto);
    void delete(UUID id);

    T findById(UUID id);
}
