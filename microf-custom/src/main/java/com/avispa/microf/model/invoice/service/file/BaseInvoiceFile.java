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

package com.avispa.microf.model.invoice.service.file;

import com.avispa.ecm.util.FormatUtils;
import com.avispa.microf.model.invoice.service.file.data.BaseInvoiceData;
import com.avispa.microf.model.invoice.service.file.data.PositionData;
import com.avispa.microf.model.invoice.service.file.data.VatRowData;
import com.avispa.microf.model.invoice.service.file.variable.VariableNameGenerator;
import com.avispa.microf.model.invoice.service.replacer.InvoiceOdtReplacer;
import com.avispa.microf.model.invoice.service.replacer.TemplateReplacer;

import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public abstract class BaseInvoiceFile<T extends BaseInvoiceData> extends OdfDocumentFile<T> {
    protected static final String TOTAL_TEXT = "Ogółem";

    private final TemplateReplacer replacer;

    protected enum PositionType {
        BEFORE_CORRECTION,
        AFTER_CORRECTION,
        CORRECTED,
        REGULAR
    }

    protected BaseInvoiceFile(String templatePath) {
        super(templatePath);
        this.replacer = new InvoiceOdtReplacer(this.textDocument);
    }

    @Override
    public void generate(T invoiceData, String issuerName) {
        Map<String, String> variables = fillVariables(invoiceData, issuerName);

        replacer.replaceVariables(variables);
    }

    protected abstract Map<String, String> fillVariables(T invoiceData, String issuerName);


    protected static void createPositionVariables(Map<String, String> variables, PositionData position, int row, String rowName) {
        createPositionVariables(variables, position, row, rowName, PositionType.REGULAR);
    }

    protected static void createPositionVariables(Map<String, String> variables, PositionData position, int row, String rowName, PositionType positionType) {
        variables.put(VariableNameGenerator.generatePositionName(row, 0), rowName);
        variables.put(VariableNameGenerator.generatePositionName(row, 1), getPositionName(position, positionType));
        variables.put(VariableNameGenerator.generatePositionName(row, 2), FormatUtils.formatMoney(position.getQuantity()));
        variables.put(VariableNameGenerator.generatePositionName(row, 3), position.getUnit());
        variables.put(VariableNameGenerator.generatePositionName(row, 4), FormatUtils.formatMoney(position.getUnitPrice()));
        variables.put(VariableNameGenerator.generatePositionName(row, 5), FormatUtils.formatMoney(position.getDiscount()));
        variables.put(VariableNameGenerator.generatePositionName(row, 6), FormatUtils.formatMoney(position.getPrice()));
        variables.put(VariableNameGenerator.generatePositionName(row, 7), FormatUtils.formatMoney(position.getNetValue()));
        String vatRate = position.getVatRateLabel();
        variables.put(VariableNameGenerator.generatePositionName(row, 8), vatRate.substring(0, vatRate.length() - 1)); // remove percent sign
    }


    private static String getPositionName(PositionData position, PositionType positionType) {
        return switch (positionType) {
            case BEFORE_CORRECTION -> "Przed korektą: " + position.getPositionName();
            case AFTER_CORRECTION -> "Po korekcie: " + position.getPositionName();
            case CORRECTED -> "Korekta: " + position.getPositionName();
            case REGULAR -> position.getPositionName();
        };
    }

    protected static void createVatRowVariables(Map<String, String> variables, VatRowData vatRow, int row, String rowName) {
        variables.put(VariableNameGenerator.generateVatMatrixName(row, 0), rowName);
        variables.put(VariableNameGenerator.generateVatMatrixName(row, 1), FormatUtils.formatMoney(vatRow.getNetValue()));
        variables.put(VariableNameGenerator.generateVatMatrixName(row, 2), FormatUtils.formatMoney(vatRow.getVat()));
        variables.put(VariableNameGenerator.generateVatMatrixName(row, 3), FormatUtils.formatMoney(vatRow.getGrossValue()));
    }
}
