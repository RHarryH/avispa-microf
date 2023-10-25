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

package com.avispa.ecm.model.ui.widget.list;

import com.avispa.ecm.model.configuration.EcmConfigRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface ListWidgetRepository extends EcmConfigRepository<ListWidget> {
    interface ListWidgetProjection {
        UUID getId();
        String getTypeName();
    }

    @Query("select lw.id as id, lw.type.objectName as typeName from ListWidget lw where lw.objectName=:objectName")
    Optional<ListWidgetProjection> findIdAndTypeNameByObjectName(String objectName);
}
