package com.avispa.ecm.model.ui.propertypage;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.configuration.propertypage.PropertyPage;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.control.Table;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.model.base.dto.Dto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class PropertyPageService {
    private final ContextService contextService;
    private final PropertyPageMapper propertyPageMapper;
    private final EcmConfigRepository<PropertyPage> propertyPageRepository;

    /**
     * Gets the content of property page by finding upsert configuration matching provided ECM object
     * and by filling the labels, combo boxes, radios and other components (if any) with data from the context DTO
     * @param ecmObject ECM object which upsert configuration will be used
     * @param contextDto DTO object used as context for property page
     * @return
     */
    public Optional<PropertyPageContent> getPropertyPage(EcmObject ecmObject, Dto contextDto) {
        return contextService.getConfiguration(ecmObject, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.convertToContent(propertyPage, contextDto, false));
    }

    public Optional<PropertyPageContent> getPropertyPage(String name, Object context) {
        return propertyPageRepository.findByObjectName(name)
                .map(propertyPage -> propertyPageMapper.convertToContent(propertyPage, context, false));
    }

    public Table getTable(String tableName, Class<? extends EcmObject> ecmObjectClass, Class<? extends Dto> contextDtoClass) {
        return contextService.getConfiguration(ecmObjectClass, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.getTable(propertyPage, tableName, contextDtoClass))
                .orElse(null);
    }
}
