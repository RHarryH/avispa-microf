package com.avispa.microf.model.invoice.service.replacer;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;

import static com.avispa.microf.model.invoice.service.file.variable.Variable.VP_POSITIONS;
import static com.avispa.microf.model.invoice.service.file.variable.Variable.VP_VAT_MATRIX;

/**
 * Contains additional invoice specific implementation for handling tables
 *
 * @author Rafał Hiszpański
 */
public final class InvoiceOdtReplacer extends OdtReplacer {
    private static final String POSITIONS_TABLE = "Positions";
    private static final String VAT_MATRIX_TABLE = "VatMatrix";

    public InvoiceOdtReplacer(OdfTextDocument document) {
        super(document);
    }

    @Override
    protected void processContent(Map<String, String> variables) throws SAXException, IOException {
        super.processContent(variables);

        NodeList nodes = getContentDom().getElementsByTagName(TableTableElement.ELEMENT_NAME.getQName());
        for(int i = 0; i < nodes.getLength(); i++) {
            TableTableElement table = (TableTableElement) nodes.item(i);
            if(table.getTableNameAttribute().equals(POSITIONS_TABLE)) {
                processTable(table, variables, VP_POSITIONS, 9);
            }
            if(table.getTableNameAttribute().equals(VAT_MATRIX_TABLE)) {
                processTable(table, variables, VP_VAT_MATRIX, 4);
            }
        }
    }
}
