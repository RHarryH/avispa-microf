package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.EcmObjectRepository;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.mapper.IExtendedEntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public abstract class BaseService<T extends EcmObject, D extends Dto, R extends EcmObjectRepository<T>, M extends IExtendedEntityDtoMapper<T, D, ? extends CommonDto>> implements IBaseService<T, D> {
    private final R repository;
    private final M entityDtoMapper;

    protected R getRepository() {
        return repository;
    }

    public M getEntityDtoMapper() {
        return entityDtoMapper;
    }

    public List<CommonDto> findAll() {
        return repository
                .findAll(Sort.by(Sort.Direction.ASC, "objectName")).stream()
                .map(entityDtoMapper::convertToCommonDto)
                .collect(Collectors.toList());
    }
}
