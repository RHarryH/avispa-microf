/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
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

package com.avispa.microf.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

public class VATINConstraintValidator implements ConstraintValidator<VATINConstraint, String> {
    @Override
    public boolean isValid(String vatin, ConstraintValidatorContext cxt) {
        if(Strings.isEmpty(vatin)) {
            return false;
        }

        if (vatin.length() == 13) {
            vatin = vatin.replace("-", "");
        }
        if (vatin.length() != 10) {
            return false;
        }
        int[] weights = {6, 5, 7, 2, 3, 4, 5, 6, 7};
        try {
            int sum = 0;
            for (int i = 0; i < weights.length; ++i) {
                sum += Integer.parseInt(vatin.substring(i, i + 1)) * weights[i];
            }
            return (sum % 11) == Integer.parseInt(vatin.substring(9, 10));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}