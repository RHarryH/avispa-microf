package com.avispa.microf.model.property.mapper;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.controls.PropertyControl;
import com.avispa.microf.model.property.PropertyControlDto;
import com.avispa.microf.util.ReflectUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        config = ControlMapper.class)
public interface PropertyControlMapper extends BaseControlMapper{
    @Mapping(target = "name", source = "control.name")
    PropertyControlDto toPropertyControlDto(PropertyControl control, @Context EcmObject object);

    @AfterMapping
    default void setControlValue(@Context EcmObject object,
                                  @MappingTarget PropertyControlDto propertyControlDto) {
        try {
            Object value = ReflectUtils.getFieldValue(object, propertyControlDto.getName());
            if(null != value) {
                if(value instanceof LocalDateTime) {
                    LocalDateTime localDateTime = (LocalDateTime)value;

                    propertyControlDto.setValue(localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
                } else {
                    propertyControlDto.setValue(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
