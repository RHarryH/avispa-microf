/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.ecm.model.ui.modal.link;

import com.avispa.ecm.model.configuration.context.ContextService;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.model.ui.modal.link.dto.LinkDocumentDto;
import com.avispa.ecm.model.ui.modal.link.mapper.LinkDocumentDtoMapper;
import com.avispa.ecm.util.exception.EcmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class LinkDocumentService {
    private final LinkDocumentDtoMapper mapper;

    private final ContextService contextService;
    private final TypeService typeService;

    /**
     * Try to get link document configuration. Throw exception in case of failure.
     *
     * @param typeName type for which the configuration should be got
     * @return
     */
    public LinkDocumentDto get(String typeName) {
        var type = typeService.getType(typeName);

        var linkDocument = contextService.getConfiguration(type.getEntityClass(), LinkDocument.class).
                orElseThrow(() -> new EcmException("Link document configuration for '" + typeName + "' type couldn't be found"));

        return mapper.convert(linkDocument);
    }

    /**
     * Try to find link document configuration. Return null in case of failure.
     *
     * @param typeName type for which the configuration should be found
     * @return
     */
    public LinkDocumentDto find(String typeName) {
        var type = typeService.getType(typeName);

        var linkDocument = contextService.getConfiguration(type.getEntityClass(), LinkDocument.class).
                orElse(null);

        return null == linkDocument ? null : mapper.convert(linkDocument);
    }
}
