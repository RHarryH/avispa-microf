package com.avispa.microf.model.invoice.service.replacer;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;
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
import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public class OdtReplacer extends AbstractReplacer{
    private static final Logger log = LoggerFactory.getLogger(OdtReplacer.class);

    private OdfTextDocument document;

    public OdtReplacer(OdfTextDocument document) {
        this.document = document;
    }

    @Override
    public void replaceVariables(Map<String, String> variables){
        try {
            OdfContentDom contentDom = document.getContentDom();
            Node rootNode = contentDom.getRootElement();
            XPath xpath = contentDom.getXPath();

            for(Map.Entry<String, String> variable : variables.entrySet()) {
                processVariable(rootNode, xpath, variable);
            }
        } catch (IOException | SAXException e) {
            log.error("Can't access document content DOM", e);
        }
    }

    /**
     * Runs XPath expression to find variable nodes and replaces their content with
     * actual variable value
     * @param rootNode root of the content DOM
     * @param xpath xpath object
     * @param variable variable name => value pair
     */
    private void processVariable(Node rootNode, XPath xpath, Map.Entry<String, String> variable) {
        String variableName = variable.getKey();

        try {
            NodeList nodes = (NodeList) xpath.evaluate("//text:variable-set[@text:name='" + variableName + "']", rootNode, XPathConstants.NODESET);
            for(int i = 0; i < nodes.getLength(); i++) {
                OdfElement element = (OdfElement) nodes.item(i);

                if(log.isDebugEnabled()) {
                    log.debug("Found '{}' content for '{}' variable", element.getTextContent(), variableName);
                }
                element.setTextContent(variable.getValue());
            }
        } catch (XPathExpressionException e) {
            log.error("Can't evaluate XPath expression to find variables in invoice template", e);
        }
    }
}
