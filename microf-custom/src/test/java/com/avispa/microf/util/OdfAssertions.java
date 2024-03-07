/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.util;

import org.odftoolkit.odfdom.pkg.OdfElement;

import static org.xmlunit.assertj3.XmlAssert.assertThat;

/**
 * @author Rafał Hiszpański
 */
public class OdfAssertions {
    private static final String TEST_START_TAG = """
            <odf-test xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0">
            """;

    private static final String TEST_END_TAG = "</odf-test>";

    public static void assertOdfElementEquals(String expected, OdfElement actual) {
        assertThat(TEST_START_TAG + expected + TEST_END_TAG).and(TEST_START_TAG + actual + TEST_END_TAG)
                .ignoreChildNodesOrder()
                .ignoreComments()
                .ignoreWhitespace()
                .areIdentical();
    }
}
