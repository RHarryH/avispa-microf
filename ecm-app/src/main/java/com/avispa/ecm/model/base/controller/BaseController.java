package com.avispa.ecm.model.base.controller;

import com.avispa.ecm.model.base.BaseService;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.base.dto.DtoService;
import com.avispa.ecm.model.base.mapper.IEntityDtoMapper;
import com.avispa.ecm.model.EcmEntityRepository;
import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.ui.modal.context.EcmAppContext;
import com.avispa.ecm.model.error.ErrorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseController<T extends EcmObject, D extends Dto, S extends BaseService<T, D, ? extends EcmEntityRepository<T>, ? extends IEntityDtoMapper<T, D>>> implements IBaseController<D>, IBaseModalableController {
    private final S service;

    private DtoService dtoService;

    @Autowired
    public void setDtoService(DtoService dtoService) {
        this.dtoService = dtoService;
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200 when everything will go fine
    @Override
    public void add(HttpServletRequest request) {
        D dto = dtoService.createEmptyDtoInstance(request);

        EcmAppContext<D> context = new EcmAppContext<>();
        context.setObject(dto);

        BindingResult result = dtoService.bindObjectToDto(request, context);

        add(context.getObject(), result);
    }

    @Override
    public void add(D dto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        T object = service.getEntityDtoMapper().convertToEntity(dto);
        service.add(object);
    }

    @PostMapping(value = "/update/{id}")
    @ResponseBody
    @Override
    public void update(HttpServletRequest request, @PathVariable("id") UUID id) {
        D dto = dtoService.createEmptyDtoInstance(request);
        dto.setId(id);

        EcmAppContext<D> context = new EcmAppContext<>();
        context.setObject(dto);

        BindingResult result = dtoService.bindObjectToDto(request, context);

        update(context.getObject(), result);
    }

    @Override
    public void update(D dto, BindingResult result) {
        if(result.hasErrors()) {
            ErrorUtil.processErrors(HttpStatus.BAD_REQUEST, result);
        }

        service.update(dto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @Override
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }

    public S getService() {
        return service;
    }
}
