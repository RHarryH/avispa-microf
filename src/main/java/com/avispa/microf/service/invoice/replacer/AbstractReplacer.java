package com.avispa.microf.service.invoice.replacer;

/**
 * @author Rafał Hiszpański
 */
public abstract class AbstractReplacer implements ITemplateReplacer {
    private static final String VARIABLE_TEMPLATE = "${%s}";

    protected String convertToVariable(String name) {
        return String.format(VARIABLE_TEMPLATE, name);
    }
}
