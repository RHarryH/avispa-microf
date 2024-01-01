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

function isValidNIP(nip) {
    const weights = [6, 5, 7, 2, 3, 4, 5, 6, 7];
    nip = nip.replace(/[\s-]/g, '');

    if (nip.length === 10 && parseInt(nip, 10) > 0) {
        let sum = 0;
        for(let i = 0; i < 9; i++){
            sum += nip[i] * weights[i];
        }
        return (sum % 11) === Number(nip[9]);
    }
    return false;
}

/**
 * Borrowed from: https://stackoverflow.com/a/35599724/2556651
 * Updated with actual list of countries and NRB check
 * @param input input
 * @returns {boolean} true if is valid IBAN
 */
function isIBAN(input) {
    const CODE_LENGTHS = {
        AD: 24, AE: 23, AL: 28, AT: 20, AZ: 28, BA: 20, BE: 16, BG: 22, BH: 22,
        BI: 27, BR: 29, BY: 28, CH: 21, CR: 22, CY: 28, CZ: 24, DE: 22, DJ: 27,
        DK: 18, DO: 28, EE: 20, EG: 29, ES: 24, FI: 18, FK: 18, FO: 18, FR: 27,
        GB: 22, GE: 22, GI: 23, GL: 18, GR: 27, GT: 28, HR: 21, HU: 28, IE: 22,
        IL: 23, IQ: 23, IS: 26, IT: 27, JO: 30, KW: 30, KZ: 20, LB: 28, LC: 32,
        LI: 21, LT: 20, LU: 20, LV: 21, LY: 25, MC: 27, MD: 24, ME: 22, MK: 19,
        MN: 20, MR: 27, MT: 31, MU: 30, NI: 28, NL: 18, NO: 15, PK: 24, PL: 28,
        PS: 29, PT: 25, QA: 29, RO: 24, RS: 22, RU: 33, SA: 24, SC: 31, SD: 18,
        SE: 24, SI: 19, SK: 24, SM: 27, SO: 23, ST: 25, SV: 28, TL: 23, TN: 24,
        TR: 26, UA: 29, VA: 22, VG: 24, XK: 20
    }

    let iban = String(input).toUpperCase().replace(/[^A-Z0-9]/g, ''); // keep only alphanumeric characters
    let code = iban.match(/^([A-Z]{2})(\d{2})([A-Z\d]+)$/); // match and capture (1) the country code, (2) the check digits, and (3) the rest

    // check syntax and length
    if (!code || iban.length !== CODE_LENGTHS[code[1]]) {
        return false;
    }

    // rearrange country code and check digits, and convert chars to ints
    let digits = (code[3] + code[1] + code[2]).replace(/[A-Z]/g, function (letter) {
        return letter.charCodeAt(0) - 55;
    });

    // final check
    const isIBAN = mod97(digits) === 1;

    if (isIBAN && code[1] === 'PL') {
        return isNRB(iban);
    } else {
        return isIBAN;
    }

    function mod97(string) {
        let checksum = string.slice(0, 2), fragment;
        for (let offset = 2; offset < string.length; offset += 7) {
            fragment = String(checksum) + string.substring(offset, offset + 7);
            checksum = parseInt(fragment, 10) % 97;
        }
        return checksum;
    }

    function isNRB(nrb) {
        const weights = [3, 9, 7, 1, 3, 9, 7, 1];

        nrb = nrb.substring(2); // remove country code

        const sortCode = nrb.substring(2, 10);

        function validateSortCode(sortCode) {
            let total = 0;
            for (let i = 0; i < sortCode.length; i++) { // without last digit
                total += sortCode.charCodeAt(i) * weights[i];
            }
            return total % 10 === 0;
        }

        return validateSortCode(sortCode);
    }
}