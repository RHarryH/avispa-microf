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

package com.avispa.microf.model.invoice.type.correction.service;

import com.avispa.microf.model.invoice.position.Position;
import com.avispa.microf.model.invoice.position.PositionDto;
import com.avispa.microf.model.invoice.position.PositionMapper;
import com.avispa.microf.model.invoice.type.correction.CorrectionInvoice;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Additional validation not possible to do on DTO level. On DTO level the validation allows to validate the inputs
 * provided on the property page. If we want to make deeper logical checks we need to do it elsewhere.
 *
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
public class CorrectionInvoiceValidator {
    public static final String VM_CORRECTION_NOT_NEEDED = "There are no changes in the positions. The correction is not needed";

    private final PositionMapper positionMapper;

    public void validate(CorrectionInvoice entity) {
        if (hasSamePositions(entity.getPositions(), entity.getOriginalInvoice().getPositions())) {
            throw new ValidationException(VM_CORRECTION_NOT_NEEDED);
        }
    }

    private boolean hasSamePositions(List<Position> correctionPositions,
                                     List<Position> originalPositions) {
        List<PositionDto> correctionPositionsDto = correctionPositions.stream().map(positionMapper::convertToDto).toList();
        List<PositionDto> originalPositionsDto = originalPositions.stream().map(positionMapper::convertToDto).toList();

        return CollectionUtils.isEqualCollection(correctionPositionsDto, originalPositionsDto);
    }
}
