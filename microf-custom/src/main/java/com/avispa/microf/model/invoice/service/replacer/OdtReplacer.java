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

package com.avispa.microf.model.invoice.service.replacer;

import com.avispa.microf.model.invoice.service.file.variable.VariableNameGenerator;
import com.avispa.microf.model.invoice.service.replacer.merge.CellMergeService;
import com.avispa.microf.model.invoice.service.replacer.merge.MergeRegion;
import lombok.extern.slf4j.Slf4j;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.text.TextVariableSetElement;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
public class OdtReplacer implements TemplateReplacer {
    private final CellMergeService cellMergeService = new CellMergeService();
    protected final OdfTextDocument document;

    public OdtReplacer(OdfTextDocument document) {
        this.document = document;
    }

    @Override
    public void replaceVariables(Map<String, String> variables){
        try {
            processHeaderAndFooter(variables);
            processContent(variables);
        } catch (IOException | SAXException e) {
            log.error("Can't access document content DOM", e);
        }
    }

    protected void processHeaderAndFooter(Map<String, String> variables) throws SAXException, IOException {
        OdfStylesDom stylesDom = getStylesDom();
        Node rootNode = stylesDom.getRootElement();
        XPath xpath = stylesDom.getXPath();

        processVariables(rootNode, xpath, variables);
    }

    protected final OdfStylesDom getStylesDom() throws SAXException, IOException {
        return document.getStylesDom();
    }

    protected void processContent(Map<String, String> variables) throws SAXException, IOException {
        OdfContentDom contentDom = getContentDom();

        Node rootNode = contentDom.getRootElement();
        XPath xpath = contentDom.getXPath();

        processVariables(rootNode, xpath, variables);
    }

    protected final OdfContentDom getContentDom() throws SAXException, IOException {
        return document.getContentDom();
    }

    /**
     * Runs XPath expression to find variable nodes and replaces their content with
     * actual variable value
     * @param rootNode root of the content DOM
     * @param xpath xpath object
     * @param variables variable name => value pair
     */
    private void processVariables(Node rootNode, XPath xpath, Map<String, String> variables) {
        try {
            NodeList nodes = (NodeList) xpath.evaluate("//text:variable-set", rootNode, XPathConstants.NODESET);
            for(int i = 0; i < nodes.getLength(); i++) {
                TextVariableSetElement element = (TextVariableSetElement) nodes.item(i);

                log.debug("Found '{}' content for '{}' variable", element.getTextContent(), element.getTextNameAttribute());

                element.setTextContent(variables.get(element.getTextNameAttribute()));
            }
        } catch (XPathExpressionException e) {
            log.error("Can't evaluate XPath expression to find variables in invoice template", e);
        }
    }

    protected final void processTable(String tableName, Map<String, String> variables, String variablePrefix, boolean mergerWithSameName) {
        OdfTable table = document.getTableByName(tableName);

        if (null == table) {
            log.warn("Table with name '{}' was not found", tableName);
            return;
        }

        processTable(table, variables, variablePrefix, mergerWithSameName);
    }

    /**
     * Creates rows in the table and populates it with values from variables. Variable names
     * are generated using {@see com.avispa.ecm.model.invoice.service.file.variable.VariableNameGenerator}.
     *
     * @param table          table element
     * @param variables      list of variables
     * @param variablePrefix prefix used for array variables
     * @param mergeSameCells true if cells in first column should be merged if contain the same value
     */
    private void processTable(OdfTable table, Map<String, String> variables, String variablePrefix, boolean mergeSameCells) {
        String sizeVariableName = variablePrefix + "_size";

        int cols = table.getColumnCount();
        int rows = Integer.parseInt(variables.get(sizeVariableName));

        createRows(table, rows - 1); // does not count template row

        for (int col = 0; col < cols; col++) {
            MergeRegion region = MergeRegion.start();

            for (int row = 1; row <= rows; row++) {  // skip header
                var cell = table.getCellByPosition(col, row);

                String variableName = VariableNameGenerator.generateArrayVariableName(variablePrefix, row - 1, col); // variables are number from 0
                String value = variables.get(variableName);
                if (col == 0 && mergeSameCells) { // only for first column
                    region = cellMergeService.checkMergeRegion(table.getCellByPosition(col, row - 1), value, region);
                }

                setCellContent(cell.getOdfElement(), value);
            }

            cellMergeService.addRegion(region); // add current region
        }

        cellMergeService.applyMerges(table);
    }

    /**
     * Creates missing row by cloning the template row
     *
     * @param table
     * @param dataSize
     */
    private void createRows(OdfTable table, int dataSize) {
        TableTableElement tableElement = table.getOdfElement();
        // there should be at least one template row containing style proper for rest of the rows
        OdfElement templateRow = tableElement.getLastChildElement();
        for (int j = 0; j < dataSize; j++) {
            OdfElement row = templateRow.cloneElement();
            tableElement.appendChild(row);
        }
    }

    private void setCellContent(Node cell, String content) {
        cell.getFirstChild().setTextContent(content);
    }
}
