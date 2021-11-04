package com.avispa.microf.model.property.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.controls.Control;
import com.avispa.microf.model.property.ControlDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@MapperConfig(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface ControlMapper extends BaseControlMapper{
    @Mapping(target = "label", source = "control.label")
    @Mapping(target = "type", expression = "java(control.getType().getName())")
    @Mapping(target = "attributes", source = "control.attributes", qualifiedByName = "convertAttributes")
    ControlDto toControlDto(Control control, @Context EcmObject object);
}
