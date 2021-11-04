package com.avispa.microf.model.property.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.controls.OrganizationControl;
import com.avispa.ecm.util.expression.ExpressionResolver;
import com.avispa.microf.model.property.ControlDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        config = ControlMapper.class)
public abstract class OrganizationControlMapper implements BaseControlMapper{
    @Autowired
    private ExpressionResolver expressionResolver;

    @Mapping(target="label", qualifiedByName = "resolveLabel")
    public abstract ControlDto toOrganizationControlDto(OrganizationControl control, @Context EcmObject object);

    @Named("resolveLabel")
    protected String resolveLabel(String label, @Context EcmObject object) {
        return expressionResolver.resolve(object, label);
    }
}
