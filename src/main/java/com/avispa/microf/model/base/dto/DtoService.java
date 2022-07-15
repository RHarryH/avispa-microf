package com.avispa.microf.model.base.dto;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import com.avispa.microf.util.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValues;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DtoService {
    private final DtoRepository dtoRepository;
    private final TypeService typeService;

    private final WebDataBinderFactory dataBinderFactory;

    private final GenericService genericService;

    /**
     * Extracts
     * @param request
     * @param <D>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <D extends Dto> D createEmptyDtoInstance(HttpServletRequest request) {
        Class<? extends EcmObject> entityClass = typeService.getType(request.getParameter("typeName")).getEntityClass();
        String typeDiscriminatorName = typeService.getTypeDiscriminatorFromAnnotation(entityClass);

        Optional<DtoObject> dtoObject;
        if(StringUtils.isNotEmpty(typeDiscriminatorName)) {
            String value = request.getParameter("object." + typeDiscriminatorName);
            dtoObject = dtoRepository.findByEntityClassAndDiscriminator(entityClass, value);
        } else {
            dtoObject = getDtoObject(entityClass);
        }

        return (D) dtoObject
                .map(d -> BeanUtils.instantiateClass(d.getDtoClass()))
                .orElseThrow();
    }

    private Optional<DtoObject> getDtoObject(Class<? extends EcmObject> entityClass) {
        Optional<DtoObject> dtoObject;
        dtoObject = dtoRepository.findByEntityClassAndDiscriminatorIsNull(entityClass);
        return dtoObject;
    }

    public DtoObject getDtoObjectFromTypeName(String typeName) {
        return dtoRepository.findByTypeNameAndDiscriminatorIsNull(typeName).orElseThrow();
    }

    public DtoObject getDtoObjectFromType(Type type) {
        return dtoRepository.findByTypeAndDiscriminatorIsNull(type).orElseThrow();
    }

    public Dto convertEntityToDto(EcmObject entity) {
        return genericService.getService(entity.getClass()).getEntityDtoMapper().convertToDto(entity);
    }

    public <D extends Dto> BindingResult bindObjectToDto(HttpServletRequest request, MicroFContext<D> context) {
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
