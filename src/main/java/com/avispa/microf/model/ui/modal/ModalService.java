package com.avispa.microf.model.ui.modal;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.upsert.Upsert;
import com.avispa.ecm.model.context.ContextService;
import com.avispa.microf.model.Dto;
import com.avispa.microf.model.property.PropertyPageDto;
import com.avispa.microf.model.property.mapper.PropertyPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ModalService {
    private final ContextService contextService;
    private final PropertyPageMapper propertyPageMapper;

    public <E extends EcmObject, D extends Dto> String constructModal(Model model, Class<E> ecmObjectClass, Class<D> contextClass, ModalConfiguration modal) {
        D contextDto = createContextDto(contextClass);
        return constructModal(model, ecmObjectClass, contextDto, modal);
    }

    private <D extends Dto> D createContextDto(Class<D> contextClass) {
        D contextDto = null;
        try {
            contextDto = contextClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Cannot instantiate context DTO object", e);
        }
        return contextDto;
    }

    public <E extends EcmObject, D extends Dto> String constructModal(Model model, Class<E> ecmObjectClass, D contextDto, ModalConfiguration modal) {
        PropertyPageDto propertyPageDto = getPropertyPage(contextDto, ecmObjectClass);

        model.addAttribute("propertyPage", propertyPageDto);
        model.addAttribute("object", contextDto);
        model.addAttribute("modal", modal);
        return "fragments/modal :: upsertModal";
    }

    /**
     * Gets property page DTO from upsert configuration matching provided ecm object
     * @param contextDto DTO object used as context for property page
     * @param ecmObjectClass ECM object class which upsert configuration will be used
     * @param <E> any object which is an ECM object
     * @return
     */
    private <E extends EcmObject> PropertyPageDto getPropertyPage(Object contextDto, Class<E> ecmObjectClass) {
        return contextService.getConfiguration(ecmObjectClass, Upsert.class)
                .map(Upsert::getPropertyPage)
                .map(propertyPage -> propertyPageMapper.convertToDto(propertyPage, contextDto, false)) // convert to dto
                .orElse(null);
    }
}
