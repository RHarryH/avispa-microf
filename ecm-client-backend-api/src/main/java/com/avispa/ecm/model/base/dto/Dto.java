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

package com.avispa.ecm.model.base.dto;

import com.avispa.ecm.model.ui.modal.context.ModalPageEcmContextInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
public interface Dto extends ModalPageEcmContextInfo {
    String EMPTY_STRING_REGEX = "^$";

    UUID getId();
    void setId(UUID id);

    default boolean isPdfRenditionAvailable() {
        return false;
    }

    @JsonIgnore
    default void setPdfRenditionAvailable(boolean pdfRenditionAvailable){}

    /**
     * Supplementary method to implement basic properties' inheritance. It allows to manipulate the properties after
     * linking the document. In a target solution, inheritance would be a separate configuration element allowing to
     * copy arbitrary properties from arbitrary documents/objects with the use of conditions.
     */
    default void inherit() {
        // NO DEFAULT IMPLEMENTATION
    }
}
