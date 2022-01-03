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
