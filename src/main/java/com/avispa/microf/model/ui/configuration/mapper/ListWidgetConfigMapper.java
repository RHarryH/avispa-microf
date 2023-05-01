package com.avispa.microf.model.ui.configuration.mapper;

import com.avispa.ecm.model.configuration.load.mapper.EcmConfigMapper;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeRepository;
import com.avispa.microf.model.ui.configuration.dto.ListWidgetConfigDto;
import com.avispa.microf.model.ui.widget.list.config.ListWidgetConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rafał Hiszpański
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class ListWidgetConfigMapper implements EcmConfigMapper<ListWidgetConfig, ListWidgetConfigDto> {
    @Autowired
    private TypeRepository typeRepository;

    @Override
    @Mapping(source = "name", target = "objectName")
    public abstract ListWidgetConfig convertToEntity(ListWidgetConfigDto dto);

    @Override
    @Mapping(source = "name", target = "objectName")
    public abstract void updateEntityFromDto(ListWidgetConfigDto dto, @MappingTarget ListWidgetConfig entity);

    protected Type typeNameToType(String typeName) {
        return typeRepository.findByTypeName(typeName);
    }
}
