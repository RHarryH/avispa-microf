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

import com.avispa.ecm.model.configuration.load.GenericLoader;
import com.avispa.ecm.model.content.ContentService;
import com.avispa.ecm.model.load.dto.ListWidgetDto;
import com.avispa.ecm.model.load.mapper.ListWidgetMapper;
import com.avispa.ecm.model.ui.widget.list.ListWidget;
import com.avispa.ecm.model.ui.widget.list.ListWidgetRepository;
import org.springframework.stereotype.Component;

/**
 * @author Rafał Hiszpański
 */
@Component
class ListWidgetLoader extends GenericLoader<ListWidget, ListWidgetDto, ListWidgetMapper> {
    protected ListWidgetLoader(ListWidgetRepository ecmConfigRepository, ListWidgetMapper ecmConfigMapper, ContentService contentService) {
        super(ecmConfigRepository, ecmConfigMapper, contentService);
    }
}
