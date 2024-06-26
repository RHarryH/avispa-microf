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

import com.avispa.ecm.model.configuration.EcmConfig;
import com.avispa.ecm.model.type.Type;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Rafał Hiszpański
 */
@Entity
@Getter
@Setter
public class ListWidget extends EcmConfig {
    @OneToOne(optional = false)
    private Type type;

    private String caption;
    private String emptyMessage;

    @OrderColumn(nullable = false)
    @Column(nullable = false)
    @OneToMany(orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ListWidgetProperty> properties;

    @Column(nullable = false, columnDefinition = "integer default 10")
    private int itemsPerPage;
}
