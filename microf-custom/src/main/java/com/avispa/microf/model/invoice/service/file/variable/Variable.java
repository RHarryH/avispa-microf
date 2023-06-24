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

package com.avispa.microf.model.invoice.service.file.variable;

/**
 * @author Rafał Hiszpański
 */
public final class Variable {

    private Variable() {
    }

    // prefixes
    public static final String VP_VAT_MATRIX = "vat_matrix";
    public static final String VP_POSITIONS = "positions";

    public static final String V_VAT_MATRIX_SIZE = VP_VAT_MATRIX + "_size";
    public static final String V_POSITIONS_SIZE = VP_POSITIONS + "_size";
}
