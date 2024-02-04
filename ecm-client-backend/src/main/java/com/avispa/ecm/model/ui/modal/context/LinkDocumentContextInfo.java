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

package com.avispa.ecm.model.ui.modal.context;

import com.avispa.ecm.model.ui.modal.link.dto.LinkDocumentDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Builder
@Jacksonized
@Getter
@Setter
@EqualsAndHashCode
public class LinkDocumentContextInfo implements ModalPageEcmContextInfo {
    private String typeName;
    @Builder.Default
    private String filter = "";
    private UUID sourceId;

    private LinkDocumentDto linkDocument;
}
