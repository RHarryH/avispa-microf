package com.avispa.microf.model.property.mapper;

import org.mapstruct.Named;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
public interface BaseControlMapper {
    @Named("convertAttributes")
    default String convertAttributes(Map<String, String> attributes) {
        // workaround for th:attr behavior
        // dummy=null will resolve to assignment of null property to dummy property
        // in result nothing will be added
        return null != attributes && !attributes.isEmpty() ? attributes.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", ")) : "dummy=null";
    }
}
