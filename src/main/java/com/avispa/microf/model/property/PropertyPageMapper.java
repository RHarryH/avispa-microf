package com.avispa.microf.model.property;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = PropertyMapper.class)
public interface PropertyPageMapper {
    @Mapping(target = "readonly", constant = "true")
    //@Mapping(target = "propertiesDto", source = "propertyPage.properties")
    PropertyPageDto convertToDto(PropertyPage propertyPage, @Context EcmObject object);

    //PropertyPage convertToEntity(PropertyPageDto propertyPageDto);
}
