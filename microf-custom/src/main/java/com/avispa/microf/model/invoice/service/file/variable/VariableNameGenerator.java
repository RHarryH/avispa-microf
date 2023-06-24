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

import static com.avispa.microf.model.invoice.service.file.variable.Variable.VP_POSITIONS;
import static com.avispa.microf.model.invoice.service.file.variable.Variable.VP_VAT_MATRIX;

/**
 * @author Rafał Hiszpański
 */
public final class VariableNameGenerator {

    private static final String UNDERSCORE = "_";

    private VariableNameGenerator() {
    }

    public static String generateVatMatrixName(int row, int cell) {
        return generateArrayVariableName(VP_VAT_MATRIX, row, cell);
    }

    public static String generatePositionName(int row, int cell) {
        return generateArrayVariableName(VP_POSITIONS, row, cell);
    }

    public static String generateArrayVariableName(String prefix, int row, int cell) {
        return prefix + UNDERSCORE + row + UNDERSCORE + cell;
    }
}
