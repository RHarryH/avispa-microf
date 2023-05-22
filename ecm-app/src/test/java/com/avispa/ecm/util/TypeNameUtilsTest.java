package com.avispa.ecm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class TypeNameUtilsTest {

    @Test
    void hyphenIdentifierToTypeNameTest() {
        String urlIdentifier = "bank-account";
        assertEquals("Bank account", TypeNameUtils.convertResourceIdToTypeName(urlIdentifier));
    }

    @Test
    void whitespaceTest() {
        String urlIdentifier = "    bank-account   ";
        assertEquals("Bank account", TypeNameUtils.convertResourceIdToTypeName(urlIdentifier));
    }
}