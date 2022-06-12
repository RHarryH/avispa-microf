package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IBaseService<T extends EcmObject, D extends Dto> {
    void add(T object);
    void update(D dto);
    void delete(UUID id);

    T findById(UUID id);
}
