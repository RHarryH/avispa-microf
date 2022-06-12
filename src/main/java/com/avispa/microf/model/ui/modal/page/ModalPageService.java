package com.avispa.microf.model.ui.modal.page;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.control.PropertyControl;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.modal.button.ModalButton;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import com.avispa.microf.model.ui.propertypage.PropertyPageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class ModalPageService {
    private final PropertyPageService propertyPageService;
    private final TypeService typeService;

    public ModalPage createSourceModalPage() {
        List<ModalButton> buttons = new ArrayList<>(1);
        buttons.add(ModalButton.createReject());
        return new ModalPage(ModalPageType.SELECT_SOURCE, buttons);
    }

    public ModalPage createInsertionModalPage() {
        List<ModalButton> buttons = new ArrayList<>(3);
        buttons.add(ModalButton.createAccept("Add"));
        buttons.add(ModalButton.createReset());
        buttons.add(ModalButton.createReject());

        return createPropertiesModalPage(buttons);
    }

    public ModalPage createUpdateModalPage() {
        List<ModalButton> buttons = new ArrayList<>(2);
        buttons.add(ModalButton.createAccept("Edit"));
        buttons.add(ModalButton.createReject());

        return createPropertiesModalPage(buttons);
    }

    private ModalPage createPropertiesModalPage(List<ModalButton> buttons) {
        return new ModalPage(ModalPageType.PROPERTIES, buttons);
    }

    public void createPropertiesPropertyPage(ModelMap modelMap, EcmObject entity, MicroFContext<Dto> context) {
        PropertyPageContent propertyPageContent = propertyPageService.getPropertyPage(entity, context.getObject());

        // always add discriminator field as hidden if not explicitly defined
        addDiscriminatorAsHiddenControl(propertyPageContent, entity);

        modelMap.addAttribute("propertyPage", propertyPageContent);
        modelMap.addAttribute("prefix", "object");
    }

    private void addDiscriminatorAsHiddenControl(PropertyPageContent propertyPageContent, EcmObject entity) {
        String discriminator = typeService.getTypeDiscriminatorFromAnnotation(entity.getClass());
        boolean propertyPageHasDiscriminator = propertyPageContent.getControls().stream()
                .filter(PropertyControl.class::isInstance)
                .map(PropertyControl.class::cast)
                .anyMatch(propertyControl -> propertyControl.getProperty().equals(discriminator));

        if(StringUtils.isNotEmpty(discriminator) && !propertyPageHasDiscriminator) {
            propertyPageContent.addHiddenControl(discriminator);
        }
    }

    public void createSelectSourcePropertyPage(ModelMap modelMap, MicroFContext<Dto> context) {
        modelMap.addAttribute("propertyPage", propertyPageService.getPropertyPage("Select source property page", context));
    }
}
