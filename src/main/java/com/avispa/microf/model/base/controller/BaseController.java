package com.avispa.microf.model.base.controller;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import com.avispa.microf.model.error.ErrorUtil;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseController<T extends EcmObject, D extends Dto, S extends BaseService<T, D, ? extends IEntityDtoMapper<T, D>>> implements IBaseController<D>, IBaseModalableController {
    private final S service;
    private WebDataBinderFactory dataBinderFactory;

    @Autowired
    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200 when everything will go fine
    @Override
    public void add(HttpServletRequest request) {
        MicroFContext<D> modalContext = new MicroFContext<>();
        modalContext.setObject(createDto(null));

        BindingResult result = bindObjectToDto(request, modalContext);

        add(modalContext.getObject(), result);
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
        MicroFContext<D> modalContext = new MicroFContext<>();

        D dto = createDto(null);
        dto.setId(id);
        modalContext.setObject(dto);

        BindingResult result = bindObjectToDto(request, modalContext);

        update(modalContext.getObject(), result);
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

    protected abstract D createDto(Map<String, Object> object);

    private BindingResult bindObjectToDto(HttpServletRequest request, MicroFContext<D> context) {
        BindingResult result = null;

        try {
            WebDataBinder binder = dataBinderFactory.createBinder(new ServletWebRequest(request), context, "context");
            result = bindObjectToDto(request, binder);
        } catch(Exception e) {
            log.error("ERROR", e);
        }

        return result;
    }

    private BindingResult bindObjectToDto(ServletRequest request, WebDataBinder binder) {
        PropertyValues propertyValues = new ServletRequestParameterPropertyValues(request);

        // bind to the target object
        binder.bind(propertyValues);

        // validate the target object
        binder.validate();

        // get BindingResult that includes any validation errors
        return binder.getBindingResult();
    }
}
