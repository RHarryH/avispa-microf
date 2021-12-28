package com.avispa.microf.model.invoice.service.replacer;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.odftoolkit.odfdom.dom.element.text.TextVariableSetElement;
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
            processHeaderAndFooter(variables);
            processContent(variables);

        } catch (IOException | SAXException e) {
            log.error("Can't access document content DOM", e);
        }
    }

    private void processHeaderAndFooter(Map<String, String> variables) throws SAXException, IOException {
        OdfStylesDom stylesDom = document.getStylesDom();
        Node rootNode = stylesDom.getRootElement();
        XPath xpath = stylesDom.getXPath();

        processVariables(rootNode, xpath, variables);
    }

    private void processContent(Map<String, String> variables) throws SAXException, IOException {
        OdfContentDom contentDom = document.getContentDom();
        Node rootNode = contentDom.getRootElement();
        XPath xpath = contentDom.getXPath();

        processVariables(rootNode, xpath, variables);
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

                if(log.isDebugEnabled()) {
                    log.debug("Found '{}' content for '{}' variable", element.getTextContent(), element.getTextNameAttribute());
                }
                element.setTextContent(variables.get(element.getTextNameAttribute()));
            }
        } catch (XPathExpressionException e) {
            log.error("Can't evaluate XPath expression to find variables in invoice template", e);
        }
    }
}
