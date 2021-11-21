package com.avispa.microf.model.invoice.service.replacer;

import java.util.Map;

/**
 * @author Rafał Hiszpański
 */
public interface ITemplateReplacer {
    void replaceVariables(Map<String, String> variables);
}
