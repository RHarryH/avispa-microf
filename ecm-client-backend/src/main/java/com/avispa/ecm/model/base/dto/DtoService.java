/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.ecm.model.base.dto;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.util.GenericService;
import com.avispa.ecm.util.TypeNameUtils;
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
        String typeName = extractTypeName(request);
        Class<? extends EcmObject> entityClass = typeService.getType(typeName).getEntityClass();
        String typeDiscriminatorName = typeService.getTypeDiscriminatorFromAnnotation(entityClass);

        Optional<DtoObject> dtoObject;
        if(StringUtils.isNotEmpty(typeDiscriminatorName)) {
            String value = request.getParameter(typeDiscriminatorName);
            dtoObject = dtoRepository.findByEntityClassAndDiscriminator(entityClass, value);
        } else {
            dtoObject = getDtoObject(entityClass);
        }

        return (D) dtoObject
                .map(d -> BeanUtils.instantiateClass(d.getDtoClass()))
                .orElseThrow();
    }

    /**
     * Extract type name from the URI
     * the pattern for the url is "v1/<type_name>/<others>"
     *
     * @param request
     * @return
     */
    private static String extractTypeName(HttpServletRequest request) {
        var requestUri = request.getRequestURI();
        var paths = requestUri.split("/");
        if(paths.length < 2) {
            throw new IllegalStateException("Cannot extract type from request path");
        } else {
            return TypeNameUtils.convertResourceNameToTypeName(paths[2]);
        }
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

    public Dto convertObjectToDto(EcmObject object) {
        return genericService.getService(object.getClass()).getEntityDtoMapper().convertToDto(object);
    }

    public <D extends Dto> BindingResult bindObjectToDto(HttpServletRequest request, D context) {
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

        binder.bind(propertyValues); // bind to the target object
        binder.validate(); // validate the target object

        // get BindingResult that includes any validation errors
        return binder.getBindingResult();
    }
}
