package com.avispa.microf.service.invoice.replacer;

import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public interface ITemplateReplacer {
    void replaceVariables(Map<String, String> variables);
}
