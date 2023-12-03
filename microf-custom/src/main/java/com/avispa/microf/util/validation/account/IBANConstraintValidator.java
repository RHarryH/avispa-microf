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

package com.avispa.microf.util.validation.account;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.IBANValidator;

/**
 * The IBAN consists of up to 34 alphanumeric characters, as follows:
 * - country code using ISO 3166-1 alpha-2 – two letters,
 * - check digits – two digits, and
 * - Basic Bank Account Number (BBAN) – up to 30 alphanumeric characters that are country-specific.
 * The check digits represent the checksum of the bank account number which is used by banking systems to confirm that
 * the number contains no simple errors.
 * @author Rafał Hiszpański
 */
public class IBANConstraintValidator implements ConstraintValidator<IBANConstraint, String> {
    private static final IBANValidator validator = IBANValidator.DEFAULT_IBAN_VALIDATOR;

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext cxt) {
        if(iban == null) {
            return false;
        }

        String cleanIban = iban.replaceAll("[^a-zA-Z0-9]", "");
        return validator.isValid(cleanIban);
    }
}
