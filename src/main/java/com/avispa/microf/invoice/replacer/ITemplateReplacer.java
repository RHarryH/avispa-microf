package com.avispa.microf.invoice.replacer;

import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public interface ITemplateReplacer {
    void replaceVariables(Map<String, String> variables);
}
