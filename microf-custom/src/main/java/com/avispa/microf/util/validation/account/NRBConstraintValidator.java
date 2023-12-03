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
import lombok.extern.slf4j.Slf4j;

/**
 * BBAN (Basic Bank Account Number) is a country specific bank account number. For Poland it is named
 * Number Rachunku Bankowego (NRB) and it has the following format:
 * CCAAAAAAAABBBBBBBBBBBBBBBB
 * where:
 * - CC - checksum (2 digits)
 * - AAAAAAAA - sort number/sort code (numer rozliczeniowy, 8 digits)
 * - BBBBBBBBBBBBBBBB - account number (16 digits)
 *
 * Sort code structure
 * - bank code (cyfrowy wyróżnik banku)
 *     - 3 digits trailed with zero for central banks, commercial banks and associations of cooperative banks
 *     (banki spółdzielcze)
 *     - 4 digits for cooperative banks or KSKOK
 * - branch code (identyfikator oddziału, 3 digits)
 * - checksum (1 digit)
 *
 * Sort code checksum creation
 * There are 7 digits. For each specific weight is assigned:
 * 3,9,7,1,3,9,7,1 (eight digit used for validation)
 * The algorithm multiplies each digit with a corresponding weight and sum the results. Then it is divided by mod 10.
 * If the result is not 0 it is subtracted from 10.
 * There are other alternate set of weights:
 * 9,7,1,3,9,7,1,3
 * 7,1,3,9,7,1,3,9
 * 1,3,9,7,1,3,9,7
 *
 * Sort code checksum validation
 * This time we're counting weighted sum of all 8 digits. If the mod 10 of the sum is equal to 0 then the
 * sort code is valid.
 *
 * NRB -> IBAN conversion:
 * CCAAAAAAAABBBBBBBBBBBBBBBB -> PLCCAAAAAAAABBBBBBBBBBBBBBBB
 *
 * Further notes
 * The sort codes can provide extra details about the bank.
 * See more here: https://ewib.nbp.pl/faces/pages/daneDoPobrania.xhtml
 * @author Rafał Hiszpański
 */
@Slf4j
public class NRBConstraintValidator implements ConstraintValidator<NRBConstraint, String> {
    private final byte[] weights = new byte[]{3, 9, 7, 1, 3, 9, 7, 1};

    @Override
    public boolean isValid(String nrb, ConstraintValidatorContext cxt) {
        if(nrb == null) {
            return false;
        }

        String cleanNrb = nrb.replaceAll("[^a-zA-Z0-9]", "");

        if(cleanNrb.startsWith("PL")) { // provided as IBAN
            log.info("NRB number provided as IBAN");
            cleanNrb = cleanNrb.substring(2);
        }

        if(cleanNrb.length() != 26) {
            log.error("The NRB number does not have the length of 26 characters");
            return false;
        }

        String sortCode = cleanNrb.substring(2, 10);

        return validateChecksum(sortCode);
    }

    private boolean validateChecksum(String sortCode) {
        int total = 0;
        for(int i = 0; i < sortCode.length(); i++) { // without last digit
            total += Character.getNumericValue(sortCode.charAt(i)) * weights[i];
        }
        return total % 10 == 0;
    }

    public int calculateChecksum(String sortCode) {
        int total = 0;
        for(int i = 0; i < sortCode.length() - 1; i++) { // without last digit
            total += Character.getNumericValue(sortCode.charAt(i)) * weights[i];
        }

        int checksum =  total % 10;
        if(checksum != 0) {
            checksum = 10 - checksum;
        }
        return checksum;
    }
}
