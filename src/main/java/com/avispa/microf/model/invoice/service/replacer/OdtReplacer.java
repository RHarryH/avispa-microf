package com.avispa.microf.model.invoice.service.replacer;

import com.avispa.microf.model.invoice.service.file.variable.VariableNameGenerator;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.text.TextVariableSetElement;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class OdtReplacer extends AbstractReplacer {
    private static final Logger log = LoggerFactory.getLogger(OdtReplacer.class);

    private final OdfTextDocument document;

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

    /**
     * Creates rows in the table and populates it with values from variables. Variable names
     * are generated using {@see com.avispa.microf.model.invoice.service.file.variable.VariableNameGenerator}.
     * @param table table element
     * @param variables list of variables
     * @param variablePrefix prefix used for array variables
     * @param cellsNum number of cells in row table
     */
    protected final void processTable(TableTableElement table, Map<String, String> variables, String variablePrefix, int cellsNum) {
        String sizeVariableName = variablePrefix + "_size";
        List<Node> rows = createRows(table, Integer.parseInt(variables.get(sizeVariableName)));

        for(int row = 0; row < rows.size(); row++) {
            Node rowNode = rows.get(row);

            for(int cell = 0; cell < cellsNum; cell++) {
                String variableName = VariableNameGenerator.generateArrayVariableName(variablePrefix, row, cell);
                setCellContent(rowNode, cell, variables.get(variableName));
            }
        }
    }

    private List<Node> createRows(TableTableElement table, int dataSize) {
        List<Node> rows = new ArrayList<>(dataSize);

        // TODO: there is a bug with getLastChildElement() - fix when update provided
        // there should be at least one template row containing style proper for rest of the
        // rows
        OdfElement templateRow = (OdfElement) table.getLastChild();
        rows.add(templateRow);
        for(int j = 1; j < dataSize; j++) { // there is n-1 rows created (template row excluded)
            OdfElement row = templateRow.cloneElement();
            rows.add(row);
            table.appendChild(row);
        }
        return rows;
    }

    private void setCellContent(Node row, int index, String content) {
        Node cell = row.getChildNodes().item(index);
        cell.getFirstChild().setTextContent(content);
    }
}
