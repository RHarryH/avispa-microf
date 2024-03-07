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

package com.avispa.microf.model.invoice.service.replacer.merge;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;

import static com.avispa.microf.util.OdfAssertions.assertOdfElementEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rafał Hiszpański
 */
class CellMergeServiceTest {
    private static final CellMergeService cellMergeService = new CellMergeService();
    private static final String EXPECTED_MERGE_TABLE = """
            <table:table table:name="MergeTable" table:style-name="MergeTable">
                <table:table-column table:number-columns-repeated="2" table:style-name="MergeTable.A"/>
                <table:table-row>
                    <table:table-cell office:value-type="string" table:style-name="MergeTable.A1">
                        <text:p text:style-name="P5">Header A</text:p>
                    </table:table-cell>
                    <table:table-cell office:value-type="string" table:style-name="MergeTable.A1">
                        <text:p text:style-name="P5">Header B</text:p>
                    </table:table-cell>
                </table:table-row>
                <table:table-row>
                    <table:table-cell office:value-type="string" table:number-columns-spanned="1" table:number-rows-spanned="2" table:style-name="MergeTable.A2">
                        <text:p text:style-name="P4">1</text:p>
                    </table:table-cell>
                    <table:table-cell office:value-type="string" table:style-name="MergeTable.B2">
                        <text:p text:style-name="P4">B</text:p>
                    </table:table-cell>
                </table:table-row>
                <table:table-row>
                    <table:covered-table-cell table:number-columns-repeated="1"/>
                    <table:table-cell office:value-type="string" table:number-columns-spanned="1" table:number-rows-spanned="2" table:style-name="MergeTable.B3">
                        <text:p text:style-name="P4">2</text:p>
                    </table:table-cell>
                </table:table-row>
                <table:table-row>
                    <table:table-cell office:value-type="string" table:style-name="MergeTable.A4">
                        <text:p text:style-name="P4">A</text:p>
                    </table:table-cell>
                    <table:covered-table-cell table:number-columns-repeated="1"></table:covered-table-cell>
                </table:table-row>
            </table:table>
            """;

    @Test
    @SneakyThrows
    void cellTest() {
        try (final OdfTextDocument textDocument = OdfTextDocument.loadDocument(getTestFile())) {
            OdfTable table = textDocument.getTableByName("MergeTable");

            for (int col = 0; col < table.getColumnCount(); col++) {
                MergeRegion region = MergeRegion.start();

                for (int row = 1; row < table.getRowCount(); row++) {  // skip header
                    var cell = table.getCellByPosition(col, row);

                    region = cellMergeService.checkMergeRegion(table.getCellByPosition(col, row - 1), cell.getDisplayText(), region);
                }

                cellMergeService.addRegion(region); // add current region
            }

            assertEquals(Set.of(
                            new MergeRegion(1, 2, 0),
                            new MergeRegion(2, 3, 1)),
                    cellMergeService.getRegions());

            cellMergeService.applyMerges(table);

            assertOdfElementEquals(EXPECTED_MERGE_TABLE, table.getOdfElement());
        }
    }

    private File getTestFile() throws URISyntaxException {
        URL res = getClass().getClassLoader().getResource("test.odt");
        assert res != null;
        return Paths.get(res.toURI()).toFile();
    }
}