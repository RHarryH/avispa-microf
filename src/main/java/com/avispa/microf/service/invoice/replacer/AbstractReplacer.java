package com.avispa.microf.service.invoice.replacer;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Rafał Hiszpański
 */
@Service
public abstract class AbstractReplacer implements ITemplateReplacer {
    private static final String VARIABLE_TEMPLATE = "${%s}";

    protected String convertToVariable(String name) {
        return String.format(VARIABLE_TEMPLATE, name);
    }
}
