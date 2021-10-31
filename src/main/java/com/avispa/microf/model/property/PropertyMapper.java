package com.avispa.microf.model.property;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.property.Property;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PropertyMapper {
    @Mapping(target = "name", source = "property.name")
    @Mapping(target = "label", source = "property.label")
    @Mapping(target = "type", expression = "java(property.getType().getName())")
    @Mapping(target = "attributes", source = "property.attributes", qualifiedByName = "convertAttributes")
    PropertyDto toPropertyDto(Property property, @Context EcmObject object);

    default String convertAttributes(Map<String, String> attributes) {
        // workaround for th:attr behavior
        // dummy=null will resolve to assignment of null property to dummy property
        // in result nothing will be added
        return attributes.isEmpty() ? "dummy=null" : attributes.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
    }

    @AfterMapping
    default void setPropertyValue(@Context EcmObject object,
                                  @MappingTarget PropertyDto propertyDto) {
        try {
            Object value = introspect(object, propertyDto.getName());
            if(null != value) {
                if(value instanceof LocalDateTime) {
                    LocalDateTime localDateTime = (LocalDateTime)value;

                    propertyDto.setValue(localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
                } else {
                    propertyDto.setValue(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Object introspect(Object object, String name) throws Exception {
        BeanInfo info = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null && pd.getName().equals(name)) {
                return reader.invoke(object);
            }
        }

        return null;
    }
}
