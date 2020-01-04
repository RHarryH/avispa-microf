package com.avispa.microf.invoice.replacer;

import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.incubator.search.InvalidNavigationException;
import org.odftoolkit.odfdom.incubator.search.TextNavigation;
import org.odftoolkit.odfdom.incubator.search.TextSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

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
        TextNavigation search;

        for(Map.Entry<String, String> variable : variables.entrySet()) {
            search = new TextNavigation(Pattern.quote(convertToVariable(variable.getKey())), document); // quoting required as variable name is used as regexp expression

            while(search.hasNext()) {
                TextSelection item= (TextSelection)search.getCurrentItem();

                try {
                    item.replaceWith(variable.getValue());
                } catch (InvalidNavigationException e) {
                    log.error("Variable replacement failed", e);
                }
            }
        }
    }
}
