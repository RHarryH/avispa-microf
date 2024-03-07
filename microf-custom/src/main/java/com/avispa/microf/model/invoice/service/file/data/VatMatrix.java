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

package com.avispa.microf.model.invoice.service.file.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Rafał Hiszpański
 */
public record VatMatrix(Map<String, VatRowData> vatRates, VatRowData summary) {
    public List<VatRowData> vatRatesList() {
        return vatRates.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue).toList();
    }

    public List<VatRowData> fullMatrix() {
        return Stream.concat(vatRatesList().stream(), Stream.of(summary)).toList();
    }
}
