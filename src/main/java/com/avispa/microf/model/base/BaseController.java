package com.avispa.microf.model.base;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.error.ErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
public abstract class BaseController<T extends EcmObject, D extends Dto, M extends IEntityDtoMapper<T, D>, S extends BaseService<T, D>> {
    private final S service;
    private final M entityDtoMapper;

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200 when everything will go fine
    public void add(D dto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        T object = entityDtoMapper.convertToEntity(dto);
        service.add(object);
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public void update(D dto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        service.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    public S getService() {
        return service;
    }

    public M getEntityDtoMapper() {
        return entityDtoMapper;
    }
}
