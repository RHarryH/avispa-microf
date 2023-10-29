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

package com.avispa.ecm.model.ui.modal;

import com.avispa.ecm.model.configuration.propertypage.content.PropertyPageContent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequiredArgsConstructor
public class ModalController implements ModalOperations {
    private final ModalService modalService;

    @Override
    public ModalDto getAddModal(String resourceName) {
        return modalService.getAddModal(resourceName);
    }

    @Override
    public ModalDto getCloneModal(String resourceName) {
        return null;
        //PropertyPageContent propertyPageContent = propertyPageService.getPropertyPage("Select source property page", context)
        //        .orElseThrow(() -> new EcmException("Property page content can't be retrieved"));
    }

    @Override
    public ModalDto getUpdateModal(String resourceName, UUID id) {
        return modalService.getUpdateModal(resourceName, id);
    }

    @Override
    public PropertyPageContent loadPage(HttpServletRequest request, int pageNumber) {
        return null;
    }
}
