package com.avispa.microf.model.ui.widget.list;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.display.DisplayService;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.base.IBaseService;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.base.dto.DtoObject;
import com.avispa.microf.model.base.dto.DtoService;
import com.avispa.microf.model.ui.widget.list.config.ListWidgetConfig;
import com.avispa.microf.model.ui.widget.list.config.ListWidgetRepository;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
import com.avispa.microf.model.ui.widget.list.dto.ListWidgetDto;
import com.avispa.microf.model.ui.widget.list.mapper.ListDataDtoMapper;
import com.avispa.microf.util.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rafał Hiszpański
 */
@RequiredArgsConstructor
@Service
public class ListWidgetService {
    private final TypeService typeService;
    private final DtoService dtoService;
    private final DisplayService displayService;

    private final ListWidgetRepository repository;
    private final ListDataDtoMapper listDataDtoMapper;

    private final List<IBaseService<? extends EcmObject, ? extends Dto>> services;

    public ListWidgetDto getAllDataFrom(String resourceId) {
        String typeName = TypeNameUtils.convertResourceIdToTypeName(resourceId);
        Type type = this.typeService.getType(typeName);

        ListWidgetDto listWidgetDto = new ListWidgetDto();
        listWidgetDto.setResourceId(resourceId);
        listWidgetDto.setTypeName(typeName);
        listWidgetDto.setDocument(Document.class.isAssignableFrom(type.getEntityClass()));

        ListWidgetConfig listWidgetConfig = repository.findByType(type).orElseThrow();
        listWidgetDto.setCaption(listWidgetConfig.getCaption());
        listWidgetDto.setEmptyMessage(listWidgetConfig.getEmptyMessage());

        listWidgetDto.setHeaders(getHeader(type, listWidgetConfig.getProperties()));
        listWidgetDto.setDataList(getData(type, listWidgetConfig.getProperties()));

        return listWidgetDto;
    }

    /**
     * Header values stored in the database are just property names. They are then mapped by searching for
     * DisplayName annotation on the properties defined in CommonDto
     * @param type  type having Common Dto where searched DisplayName is defined
     * @param properties list of properties which should be visible on the list widget
     * @return
     */
    private List<String> getHeader(Type type, List<String> properties) {
        DtoObject dtoObject = dtoService.getDtoObjectFromType(type);

        return properties.stream()
                .map(property -> displayService.getValueFromAnnotation(dtoObject.getDtoClass(), property))
                .collect(Collectors.toList());
    }

    /**
     * Finds all data belonging to provided type and converts it to list format
     * @param type
     * @param properties
     * @return
     */
    private List<ListDataDto> getData(Type type, List<String> properties) {
        List<CommonDto> commonDtoList = findAll(type.getEntityClass());

        return commonDtoList.stream()
                .map(commonDto -> listDataDtoMapper.convert(commonDto, properties))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<CommonDto> findAll(Class<? extends EcmObject> entityClass) {
        IBaseService<EcmObject, Dto> foundService =
                (IBaseService<EcmObject, Dto>) services.stream().filter(m -> {
                    Class<?> serviceEntityClass = getServiceEntity(m.getClass());
                    return entityClass.equals(serviceEntityClass);
                }).findFirst().orElseThrow();

        return foundService.findAll();
    }

    private Class<?> getServiceEntity(Class<?> serviceClass) {
        Map<TypeVariable<?>, java.lang.reflect.Type> typeArgs  = TypeUtils.getTypeArguments(serviceClass, IBaseService.class);
        TypeVariable<?> argTypeParam =  IBaseService.class.getTypeParameters()[0];
        java.lang.reflect.Type argType = typeArgs.get(argTypeParam);
        return TypeUtils.getRawType(argType, null);
    }
}
