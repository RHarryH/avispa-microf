/*
 * Avispa ECM - a small framework for implementing basic ECM solution
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

package com.avispa.ecm.util;

import com.avispa.ecm.model.base.dto.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Getter
@Setter
public class TestDocumentDto implements Dto {
    private UUID id;

    private String objectName;
    private String issueDate;
    private boolean pdfRenditionAvailable;
}
