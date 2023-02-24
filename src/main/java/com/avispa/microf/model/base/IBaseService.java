package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.base.dto.Dto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface IBaseService<T extends EcmObject, D extends Dto> {
    @Transactional
    void add(T object);

    @Transactional
    void update(D dto);

    void delete(UUID id);

    T findById(UUID id);

    List<CommonDto> findAll();
}
