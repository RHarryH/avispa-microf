package com.avispa.microf.model.base.controller;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.base.BaseService;
import com.avispa.microf.model.base.dto.DtoRepository;
import com.avispa.microf.model.base.dto.IDto;
import com.avispa.microf.model.base.mapper.IEntityDtoMapper;
import com.avispa.microf.model.error.ErrorUtil;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseController<T extends EcmObject, D extends IDto, S extends BaseService<T, D, ? extends IEntityDtoMapper<T, D>>> implements IBaseController<D>, IBaseModalableController {
    private final S service;

    private TypeService typeService;
    private DtoRepository dtoRepository;
    private WebDataBinderFactory dataBinderFactory;

    @Autowired
    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
    }

    @Autowired
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }

    @Autowired
    public void setDtoRepository(DtoRepository dtoRepository) {
        this.dtoRepository = dtoRepository;
    }

    @PostMapping(value = "/add")
    @ResponseBody // it will just return status 200 when everything will go fine
    @Override
    public void add(HttpServletRequest request) {
        MicroFContext<D> modalContext = new MicroFContext<>();
        modalContext.setObject(createDto(request));

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

        D dto = createDto(request);
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

    @SuppressWarnings("unchecked")
    protected D createDto(HttpServletRequest request) {
        Class<? extends EcmObject> entityClass = typeService.getType(request.getParameter("typeName")).getEntityClass();
        String typeDiscriminatorName = typeService.getTypeDiscriminatorFromAnnotation(entityClass);

        if(StringUtils.isNotEmpty(typeDiscriminatorName)) {
            String value = request.getParameter("object." + typeDiscriminatorName);
            return dtoRepository
                    .findByEntityClassAndDiscriminator(entityClass, value)
                    .map(d -> (D) BeanUtils.instantiateClass(d.getDtoClass()))
                    .orElseGet(this::createDefaultDto);
        }

        return createDefaultDto();
    }

    protected abstract D createDefaultDto();

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
