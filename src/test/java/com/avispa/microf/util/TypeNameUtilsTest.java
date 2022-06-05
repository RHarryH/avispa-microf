package com.avispa.microf.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class TypeNameUtilsTest {

    @Test
    void hyphenIdentifierToTypeNameTest() {
        String urlIdentifier = "bank-account";
        assertEquals("Bank account", TypeNameUtils.convertURLIdentifierToTypeName(urlIdentifier));
    }

    @Test
    void whitespaceTest() {
        String urlIdentifier = "    bank-account   ";
        assertEquals("Bank account", TypeNameUtils.convertURLIdentifierToTypeName(urlIdentifier));
    }
}