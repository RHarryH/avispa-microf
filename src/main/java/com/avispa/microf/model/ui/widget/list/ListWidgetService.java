package com.avispa.microf.model.ui.widget.list;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.display.DisplayService;
import com.avispa.ecm.model.document.Document;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.base.dto.CommonDto;
import com.avispa.microf.model.base.dto.DtoObject;
import com.avispa.microf.model.base.dto.DtoService;
import com.avispa.microf.model.ui.widget.list.config.ListWidgetConfig;
import com.avispa.microf.model.ui.widget.list.config.ListWidgetRepository;
import com.avispa.microf.model.ui.widget.list.dto.ListDataDto;
import com.avispa.microf.model.ui.widget.list.dto.ListWidgetDto;
import com.avispa.microf.model.ui.widget.list.mapper.ListDataDtoMapper;
import com.avispa.microf.util.GenericService;
import com.avispa.microf.util.TypeNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private final GenericService genericService;

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
                .map(property -> displayService.getDisplayValueFromAnnotation(dtoObject.getDtoClass(), property))
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

    private List<CommonDto> findAll(Class<? extends EcmObject> entityClass) {
        return genericService.getService(entityClass).findAll();
    }
}
