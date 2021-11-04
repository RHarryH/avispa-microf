package com.avispa.microf.model.property.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.controls.Control;
import com.avispa.ecm.model.configuration.propertypage.controls.OrganizationControl;
import com.avispa.ecm.model.configuration.propertypage.controls.PropertyControl;
import com.avispa.microf.model.property.ControlDto;
import com.avispa.microf.model.property.PropertyPageDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = ControlMapper.class)
public abstract class/*interface*/ PropertyPageMapper {
    @Mapping(target = "readonly", constant = "true")
    // TODO: use when new version of mapstruct will be released
    //@SubClassMapping(source = OrganizationControl.class, target = ControlMapper.class)
    //@SubClassMapping(source = PropertyControl.class, target = PropertyControlMapper.class)
    // TODO: remove when new version of mapstruct will be released
    @Mapping(target = "controls", qualifiedByName = "controlListToControlDtoList")
    public abstract PropertyPageDto convertToDto(PropertyPage propertyPage, @Context EcmObject object);

    // TODO: remove when new version of mapstruct will be released
    @Autowired
    private OrganizationControlMapper organizationControlMapper;
    @Autowired
    private PropertyControlMapper propertyControlMapper;

    @Named("controlListToControlDtoList")
    protected List<ControlDto> controlListToControlDtoList(List<Control> list, @Context EcmObject object) {
        if ( list == null ) {
            return null;
        }

        List<ControlDto> list1 = new ArrayList<ControlDto>( list.size() );
        for ( Control control : list ) {
            ControlDto controlDto = null;
            if(control instanceof PropertyControl) {
                controlDto = propertyControlMapper.toPropertyControlDto((PropertyControl) control, object );
            } else if(control instanceof OrganizationControl) {
                controlDto = organizationControlMapper.toOrganizationControlDto((OrganizationControl) control, object );
            }
            list1.add( controlDto );
        }

        return list1;
    }

    //PropertyPage convertToEntity(PropertyPageDto propertyPageDto);
}
