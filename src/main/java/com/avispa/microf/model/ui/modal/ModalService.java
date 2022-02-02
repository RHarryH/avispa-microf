package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.control.Control;
import com.avispa.ecm.model.configuration.propertypage.content.control.Table;
import com.avispa.ecm.model.configuration.propertypage.content.mapper.PropertyPageMapper;
import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.DtoService;
import com.avispa.microf.model.TypedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModalService {
    private final DtoService dtoService;
    private final ContextService contextService;
    private final PropertyPageMapper propertyPageMapper;

    /**
     * Returns modal instance. Useful for inserts. Creates new empty instance of DTO object.
     * @param model
     * @param contextClass class of DTO object
     * @param modal modal configuration
     * @param <D>
     * @return
     */
    public <T extends EcmObject, D extends TypedDto<T>> String constructModal(Model model, Class<D> contextClass, ModalConfiguration modal) {
        D contextDto = dtoService.createNew(contextClass);
        return constructModal(model, contextDto, modal);
    }

    /**
     * Returns modal instance. This version of method works with already existing DTOs. Useful for
     * updates.
     * @param model
     * @param contextTypedDto DTO object
     * @param modal modal configuration
     * @param <D>
     * @return
     */
    public <T extends EcmObject, D extends TypedDto<T>> String constructModal(Model model, D contextTypedDto, ModalConfiguration modal) {
        PropertyPageContent propertyPageContent = getPropertyPage(contextTypedDto.getEntityClass(), contextTypedDto);

        model.addAttribute("propertyPage", propertyPageContent);
        model.addAttribute("object", contextTypedDto);
        model.addAttribute("modal", modal);

        return "fragments/modal :: upsertModal";
    }

    public String getTemplateRow(Model model, String tableName, Class<? extends TypedDto<?>> contextTypedDto, Class<? extends TypedDto<?>> anotherTypedDto) {
        TypedDto<?> dto = dtoService.createNew(contextTypedDto);
        PropertyPageContent propertyPageContent = getPropertyPage(dto.getEntityClass(), dto);

        Control table = propertyPageContent.getControls().stream()
                .filter(Table.class::isInstance)
                .map(Table.class::cast)
                .filter(table1 -> table1.getProperty().equals(tableName))
                .findFirst().orElseThrow();

        model.addAttribute("control", table);
        model.addAttribute("readonly", propertyPageContent.isReadonly());
        model.addAttribute("object", dtoService.createNew(anotherTypedDto));

        return "fragments/property-page :: table-template-row";
    }

    /**
     * Gets property page DTO from upsert configuration matching provided ecm object
     * @param ecmObjectClass ECM object class which upsert configuration will be used
     * @param contextDto DTO object used as context for property page
     * @return
     */
    public PropertyPageContent getPropertyPage(Class<? extends EcmObject> ecmObjectClass, TypedDto<? extends EcmObject> contextDto) {
        return contextService.getConfiguration(ecmObjectClass, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.convertToContent(propertyPage, contextDto, false)) // convert to dto
                .orElse(null);
    }
}
