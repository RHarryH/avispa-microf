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

import lombok.Builder;
import lombok.Getter;

/**
 * @author Rafał Hiszpański
 */
@Builder
@Getter
public class ModalConfiguration {
    private String id;
    private String title;
    private String action;
    private String size;
    private ModalMode mode;

    public boolean isInsertionModal() {
        return this.mode.equals(ModalMode.INSERT);
    }

    public boolean isUpdateModal() {
        return this.mode.equals(ModalMode.UPDATE);
    }

    public boolean isCloneModal() {
        return this.mode.equals(ModalMode.CLONE);
    }

    public static ModalConfigurationBuilder builder(ModalMode mode) {
        return new ModalConfigurationBuilder().mode(mode);
    }
}
