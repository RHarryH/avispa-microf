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

import lombok.NoArgsConstructor;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rafał Hiszpański
 */
@NoArgsConstructor
public class CellMergeService {

    private final Set<MergeRegion> regions = new HashSet<>();

    /**
     * Checks if the cell above the current processing cell contains the same value. If yes then the current merge
     * region should be expanded by additional row. Otherwise, the current region should be added to the list of the
     * regions and new region should be created.
     *
     * @param cellAbove cell above te current cell
     * @param value     value of current cell
     * @param region    current region
     * @return
     */
    public MergeRegion checkMergeRegion(OdfTableCell cellAbove, String value, MergeRegion region) {
        if (cellAbove.getDisplayText().equals(value)) {
            region.incrementEndRow();

            // entirely remove cell content, so it won't be duplicated after merge
            cellAbove.getOdfElement().removeChild(cellAbove.getOdfElement().getFirstChild());
        } else {
            addRegion(region);
            region = new MergeRegion(cellAbove.getRowIndex() + 1, cellAbove.getColumnIndex());
        }
        return region;
    }

    public void addRegion(MergeRegion region) {
        if (region.getStartRow() != region.getEndRow()) { // add region only if it is not a one cell region
            regions.add(region);
        }
    }

    /**
     * Applies merges on the table
     *
     * @param table table, which should be updated
     */
    public void applyMerges(OdfTable table) {
        for (MergeRegion region : regions) {
            var columnsRange = table.getCellRangeByPosition(region.getColumn(), region.getStartRow(), region.getColumn(), region.getEndRow());
            columnsRange.merge();
        }
        clear();
    }

    public Set<MergeRegion> getRegions() {
        return Collections.unmodifiableSet(regions);
    }

    private void clear() {
        regions.clear();
    }
}
