package com.avispa.ecm.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.base.dto.Dto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
interface IBaseService<T extends EcmObject, D extends Dto> {
    @Transactional
    void add(T object);

    @Transactional
    void update(D dto);

    void delete(UUID id);

    T findById(UUID id);

    List<D> findAll();
}
