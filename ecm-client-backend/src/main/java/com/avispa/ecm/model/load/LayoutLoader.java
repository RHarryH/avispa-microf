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

package com.avispa.ecm.model.load;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import com.avispa.ecm.model.configuration.load.JsonContentGenericLoader;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.load.dto.LayoutDto;
import com.avispa.ecm.model.load.mapper.LayoutMapper;
import com.avispa.ecm.model.ui.layout.Layout;
import com.avispa.ecm.util.json.JsonValidator;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
class LayoutLoader extends JsonContentGenericLoader<Layout, LayoutDto, LayoutMapper> {
    protected LayoutLoader(EcmConfigRepository<Layout> ecmConfigRepository, LayoutMapper ecmConfigMapper, ContentService contentService, JsonValidator jsonValidator) {
        super(ecmConfigRepository, ecmConfigMapper, contentService, jsonValidator, "/json-schemas/layout/layout-content.json");
    }
}
