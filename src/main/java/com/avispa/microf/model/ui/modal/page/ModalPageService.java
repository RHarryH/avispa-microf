package com.avispa.microf.model.ui.modal.page;

import com.avispa.ecm.model.EcmObject;
import com.avispa.microf.model.base.dto.Dto;
import com.avispa.microf.model.ui.modal.button.ModalButton;
import com.avispa.microf.model.ui.modal.context.MicroFContext;
import com.avispa.microf.model.ui.propertypage.PropertyPageService;
import lombok.RequiredArgsConstructor;
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
        modelMap.addAttribute("propertyPage", propertyPageService.getPropertyPage(entity, context.getObject()));
        modelMap.addAttribute("prefix", "object");
    }

    public void createSelectSourcePropertyPage(ModelMap modelMap, MicroFContext<Dto> context) {
        modelMap.addAttribute("propertyPage", propertyPageService.getPropertyPage("Select source property page", context));
    }
}
