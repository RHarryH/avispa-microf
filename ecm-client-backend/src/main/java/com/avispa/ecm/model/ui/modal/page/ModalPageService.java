/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.ecm.model.ui.modal.page;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import com.avispa.ecm.model.configuration.propertypage.content.control.PropertyControl;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.util.exception.EcmException;
import com.avispa.ecm.model.base.dto.Dto;
import com.avispa.ecm.model.ui.modal.button.ModalButton;
import com.avispa.ecm.model.ui.modal.context.EcmAppContext;
import com.avispa.ecm.model.ui.propertypage.PropertyPageService;
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

    public void createPropertiesPropertyPage(ModelMap modelMap, EcmObject entity, EcmAppContext<Dto> context) {
        PropertyPageContent propertyPageContent = propertyPageService.getPropertyPage(entity, context.getObject())
                .orElseThrow(() -> new EcmException("Property page content can't be retrieved"));

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

    public void createSelectSourcePropertyPage(ModelMap modelMap, EcmAppContext<Dto> context) {
        PropertyPageContent propertyPageContent = propertyPageService.getPropertyPage("Select source property page", context)
                        .orElseThrow(() -> new EcmException("Property page content can't be retrieved"));

        modelMap.addAttribute("propertyPage", propertyPageContent);
    }
}
